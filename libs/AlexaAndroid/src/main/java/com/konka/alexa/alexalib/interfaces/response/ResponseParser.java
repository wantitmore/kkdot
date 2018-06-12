package com.konka.alexa.alexalib.interfaces.response;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.konka.alexa.alexalib.beans.ListTemplate1Bean;
import com.konka.alexa.alexalib.beans.PlayerInfoBean;
import com.konka.alexa.alexalib.beans.Template1Bean;
import com.konka.alexa.alexalib.beans.Template2Bean;
import com.konka.alexa.alexalib.beans.WeatherTemplateBean;
import com.konka.alexa.alexalib.data.Directive;
import com.konka.alexa.alexalib.interfaces.AvsException;
import com.konka.alexa.alexalib.interfaces.AvsItem;
import com.konka.alexa.alexalib.interfaces.AvsResponse;
import com.konka.alexa.alexalib.interfaces.Display.AvsTemplateItem;
import com.konka.alexa.alexalib.interfaces.alerts.AvsDeleteAlertItem;
import com.konka.alexa.alexalib.interfaces.alerts.AvsSetAlertItem;
import com.konka.alexa.alexalib.interfaces.audioplayer.AvsPlayAudioItem;
import com.konka.alexa.alexalib.interfaces.audioplayer.AvsPlayRemoteItem;
import com.konka.alexa.alexalib.interfaces.errors.AvsResponseException;
import com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsMediaNextCommandItem;
import com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsMediaPauseCommandItem;
import com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsMediaPlayCommandItem;
import com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsMediaPreviousCommandItem;
import com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsReplaceAllItem;
import com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsReplaceEnqueuedItem;
import com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsStopItem;
import com.konka.alexa.alexalib.interfaces.speaker.AvsAdjustVolumeItem;
import com.konka.alexa.alexalib.interfaces.speaker.AvsSetMuteItem;
import com.konka.alexa.alexalib.interfaces.speaker.AvsSetVolumeItem;
import com.konka.alexa.alexalib.interfaces.speechrecognizer.AvsExpectSpeechItem;
import com.konka.alexa.alexalib.interfaces.speechrecognizer.AvsStopCaptureItem;
import com.konka.alexa.alexalib.interfaces.speechsynthesizer.AvsSpeakItem;
import com.konka.alexa.alexalib.interfaces.system.AvsSetEndpointItem;

import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static okhttp3.internal.Util.UTF_8;

/**
 * Static helper class to parse incoming responses from the Alexa server and generate a corresponding
 * {@link AvsResponse} item with all the directives matched to their audio streams.
 *
 * @author will on 5/21/2016.
 */
public class ResponseParser {

    public static final String TAG = "ResponseParser";
    public static boolean RECOGNIZE_STATE = false;
    private static final int INTEVAR = 8000;//if exceed the time,the UI switch to Control-only

    private static final Pattern PATTERN = Pattern.compile("<(.*?)>");
    public static String kkDirective;
    static List<String> nameSpaceList = new ArrayList<>();

    private final static HashMap<String, Class> mRenderTypeMap = new HashMap() {{
        put("BodyTemplate1", Template1Bean.class);
        put("BodyTemplate2", Template2Bean.class);
        put("ListTemplate1", ListTemplate1Bean.class);
        put("WeatherTemplate", WeatherTemplateBean.class);
        put("RenderPlayerInfo", PlayerInfoBean.class);
    }};
    private static Timer mTimer;

    /**
     * Get the AvsItem associated with a Alexa API post/get, this will contain a list of {@link AvsItem} directives,
     * if applicable.
     *
     * Includes hacky work around for PausePrompt items suggested by Eric@Amazon
     * @see <a href="https://forums.developer.amazon.com/questions/28021/response-about-the-shopping-list.html">Forum Discussion</a>
     *
     * @param stream the input stream as a result of our  OkHttp post/get calls
     * @param boundary the boundary we're using to separate the multiparts
     * @return the parsed AvsResponse
     * @throws IOException
     */

    public static AvsResponse parseResponse(InputStream stream, String boundary) throws IOException, IllegalStateException, AvsException {
        return parseResponse(stream, boundary, false);
    }

    public static AvsResponse parseResponse(InputStream stream, String boundary, boolean checkBoundary) throws IOException, IllegalStateException, AvsException {
        long start = System.currentTimeMillis();

        List<Directive> directives = new ArrayList<>();
        HashMap<String, ByteArrayInputStream> audio = new HashMap<>();

        byte[] bytes;
        try {
            bytes = IOUtils.toByteArray(stream);
        } catch (IOException exp) {
            exp.printStackTrace();
            Log.e(TAG, "Error copying bytes[]");
            return new AvsResponse();
        }

        String responseString = string(bytes);

        //only print json part to avoid print unprintable audio stream data
        if( responseString.indexOf("Content-ID:") == -1) {
            Log.i(TAG, "response is " + responseString);
        }else{
            Log.i(TAG, "response is " + responseString.substring(0,responseString.indexOf("Content-ID:")));
        }

        if (checkBoundary) {
            final String responseTrim = responseString.trim();
            final String testBoundary = "--" + boundary;
            if (!StringUtils.isEmpty(responseTrim) && StringUtils.endsWith(responseTrim, testBoundary) && !StringUtils.startsWith(responseTrim, testBoundary)) {
                responseString = "--" + boundary + "\r\n" + responseString;
                bytes = responseString.getBytes();
            }
        }

        MultipartStream mpStream = new MultipartStream(new ByteArrayInputStream(bytes), boundary.getBytes(), 100000, null);

        //have to do this otherwise mpStream throws an exception
        if (mpStream.skipPreamble()) {
            Log.i(TAG, "Found initial boundary: true");

            //we have to use the count hack here because otherwise readBoundary() throws an exception
            int count = 0;
            while (count < 1 || mpStream.readBoundary()) {
                String headers;
                try {
                    headers = mpStream.readHeaders();
                } catch (MultipartStream.MalformedStreamException exp) {
                    break;
                }
                ByteArrayOutputStream data = new ByteArrayOutputStream();
                mpStream.readBodyData(data);
                if (!isJson(headers)) {
                    // get the audio data
                    //convert our multipart into byte data
                    String contentId = getCID(headers);
                    if(contentId != null) {
                        Matcher matcher = PATTERN.matcher(contentId);
                        if (matcher.find()) {
                            String currentId = "cid:" + matcher.group(1);
                            audio.put(currentId, new ByteArrayInputStream(data.toByteArray()));
                        }
                    }
                } else {
                    // get the json directive
                    try {
                        String directive = data.toString(Charset.defaultCharset().displayName());
                        boolean isCardData = isCard(directive);
                        JSONObject jsonObject;

                            jsonObject = new JSONObject(directive);
                        JSONObject jsonHeader = jsonObject.getJSONObject("directive").getJSONObject("header");
                        final String nameSpace = jsonHeader.getString("namespace");
                        String name = jsonHeader.getString("name");
                        Log.d(TAG, "parseResponse: nameSpace is " + nameSpace + ", name is " + name);
                        nameSpaceList.add(nameSpace);
                        if ("AudioPlayer".equals(nameSpace) && "Play".equals(name)) {
                            nameSpaceList.clear();
                            if (mTimer != null) {
                                mTimer.cancel();
                            }
                            mTimer = new Timer();
                            mTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (nameSpaceList.size() == 0) {
                                        EventBus.getDefault().post("Controls-Only");
                                    }
                                    for (int i = 0; i< nameSpaceList.size(); i++) {
                                        if ("TemplateRuntime".equals(nameSpaceList.get(i))) {
                                            nameSpaceList.clear();
                                            break;
                                        }
                                        nameSpaceList.clear();
                                        Log.d(TAG, "parseResponse: 111111111111111--------");
                                        EventBus.getDefault().post("Controls-Only");
                                    }
                                    mTimer.cancel();
                                }
                            }, INTEVAR);
                        }
                            if ("SpeechRecognizer".equals(nameSpace)) {
                                RECOGNIZE_STATE = true;
                            } else {
                                RECOGNIZE_STATE = false;
                            }
                            if (isCardData) {
                                Class targetClazz;
                                String headName = jsonHeader.getString("name");
                                targetClazz = mRenderTypeMap.get(headName);
                                if (targetClazz == null) {
                                    String renderType = jsonObject.getJSONObject("directive").getJSONObject("payload").getString("type");
                                    targetClazz = mRenderTypeMap.get(renderType);
                                }
                                Gson gson = new Gson();
                                Object renderObj = gson.fromJson(directive, targetClazz);
                                EventBus.getDefault().post(renderObj);
                            }
                        directives.add(getDirective(directive));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                count++;
            }

        } else {
            Log.i(TAG, "Response Body: \n" + string(bytes));
            try {
                directives.add(getDirective(responseString));
            }catch (JsonParseException e) {
                e.printStackTrace();
                throw new AvsException("Response from Alexa server malformed. ");
            }
        }

        AvsResponse response = new AvsResponse();

        for (Directive directive: directives) {

            if(directive.isPlayBehaviorReplaceAll()){
                response.add(0, new AvsReplaceAllItem(directive.getPayload().getToken()));
            }
            if(directive.isPlayBehaviorReplaceEnqueued()){
                response.add(new AvsReplaceEnqueuedItem(directive.getPayload().getToken()));
            }

            AvsItem item = parseDirective(directive, audio);

            if(item != null){
                response.add(item);
            }
        }

        Log.i(TAG, "Parsing response took: " + (System.currentTimeMillis() - start) +" size is " + response.size());

        if(response.size() == 0){
            Log.i(TAG, string(bytes));
        }

        return response;
    }

    public static AvsItem parseDirective(Directive directive) throws IOException {
        return parseDirective(directive, null);
    }

    public static AvsItem parseDirective(Directive directive, HashMap<String, ByteArrayInputStream> audio) throws IOException {
        Log.i(TAG, "Parsing directive type: "+directive.getHeader().getNamespace()+":"+directive.getHeader().getName());
        switch (directive.getHeader().getName()) {
            case Directive.TYPE_SPEAK:
                String cid = directive.getPayload().getUrl();
                return new AvsSpeakItem(directive.getPayload().getToken(), cid, audio.get(cid));
            case Directive.TYPE_PLAY:
                String url = directive.getPayload().getAudioItem().getStream().getUrl();
                Log.d(TAG, "parseDirective: url is " + url + "\n" + directive.getPayload().getAudioItem().getStream().getOffsetInMilliseconds());
                if(url.contains("cid:")){
                    return new AvsPlayAudioItem(directive.getPayload().getToken(), url, audio.get(url));
                }else{
                   if(url.contains("opml.radiotime.com")) {
                        try {
                            //opml.radiotime.com provides M3U file for audio play
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();
                            Response response = client.newCall(request).execute();
                            url = response.body().string().trim();
                            Log.d(TAG, "real url:" + url);
                        } catch (Exception e) {
                            Log.d(TAG, e.toString());
                        }
                    }
                    String token = directive.getPayload().getToken();

                    long offset = directive.getPayload().getAudioItem().getStream().getOffsetInMilliseconds();
                    long progressReportDelay =  0;
                    long progressReportInterval = 0;
                    Directive.ProgressReport progressReport = directive.getPayload().getAudioItem().getStream().getProgressReport();
                    if(progressReport != null){
                        progressReportDelay = progressReport.getProgressReportDelayInMilliseconds();
                        progressReportInterval = progressReport.getProgressReportIntervalInMilliseconds();
                    }


                    return new AvsPlayRemoteItem(token, url,offset,progressReportDelay,progressReportInterval);
                }
            case Directive.TYPE_STOP_CAPTURE:
                return new AvsStopCaptureItem(directive.getPayload().getToken());
            case Directive.TYPE_STOP:
                //stop play
                return new AvsStopItem(directive.getPayload().getToken());
            case Directive.TYPE_SET_ALERT:
                return new AvsSetAlertItem(directive.getPayload().getToken(), directive);
            case Directive.TYPE_DELETE_ALERT:
                return new AvsDeleteAlertItem(directive.getPayload().getToken());
            case Directive.TYPE_SET_MUTE:
                return new AvsSetMuteItem(directive.getPayload().getToken(), directive.getPayload().isMute());
            case Directive.TYPE_SET_VOLUME:
                return new AvsSetVolumeItem(directive.getPayload().getToken(), directive.getPayload().getVolume());
            case Directive.TYPE_ADJUST_VOLUME:
                return new AvsAdjustVolumeItem(directive.getPayload().getToken(), directive.getPayload().getVolume());
            case Directive.TYPE_EXPECT_SPEECH:
                return new AvsExpectSpeechItem(directive.getPayload().getToken(), directive.getPayload().getTimeoutInMilliseconds());
            case Directive.TYPE_MEDIA_PLAY:
                return new AvsMediaPlayCommandItem(directive.getPayload().getToken());
            case Directive.TYPE_MEDIA_PAUSE:
                return new AvsMediaPauseCommandItem(directive.getPayload().getToken());
            case Directive.TYPE_MEDIA_NEXT:
                return new AvsMediaNextCommandItem(directive.getPayload().getToken());
            case Directive.TYPE_MEDIA_PREVIOUS:
                return new AvsMediaPreviousCommandItem(directive.getPayload().getToken());
            case Directive.TYPE_SET_ENDPOINT:
                return new AvsSetEndpointItem(directive.getPayload().getToken(), directive.getPayload().getEndpoint());
            case Directive.TYPE_EXCEPTION:
                return new AvsResponseException(directive);
            case Directive.TYPE_RENDER_TEMPLATE:
                // display the info to screen
                Log.d(TAG, "parseDirective: TYPE_RENDER_TEMPLATE");
//                showDisplayCard(directive)
                return new AvsTemplateItem(directive.getPayload().getToken());

            default:
                Log.e(TAG, "Unknown type found");
                return null;
        }
    }

    public static String getBoundary(Response response) throws IOException {
        Headers headers = response.headers();
        String header = headers.get("content-type");
        String boundary = "";

        if (header != null) {
            Pattern pattern = Pattern.compile("boundary=(.*?);");
            Matcher matcher = pattern.matcher(header);
            if (matcher.find()) {
                boundary = matcher.group(1);
            }
        } else {
            Log.i(TAG, "Body: " + response.body().string());
        }
        return boundary;
    }


    private static final String string(byte[] bytes) throws IOException {
        return new String(bytes, UTF_8);
    }

    /**
     * Parse our directive using Gson into an object
     * @param directive the string representation of our JSON object
     * @return the reflected directive
     */
    public static Directive getDirective(String directive) throws AvsException, IllegalStateException {
        Log.i(TAG, directive);
        Gson gson = new Gson();
        Directive.DirectiveWrapper wrapper = gson.fromJson(directive, Directive.DirectiveWrapper.class);
        if (wrapper.getDirective() == null) {
            return gson.fromJson(directive, Directive.class);
        }
        return wrapper.getDirective();
    }


    /**
     * Get the content id from the return headers from the AVS server
     * @param headers the return headers from the AVS server
     * @return a string form of our content id
     */
    private static String getCID(String headers) throws IOException {
        final String contentString = "Content-ID:";
        BufferedReader reader = new BufferedReader(new StringReader(headers));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith(contentString)) {
                return line.substring(contentString.length()).trim();
            }
        }
        return null;
    }

    /**
     * Check if the response is JSON (a validity check)
     * @param headers the return headers from the AVS server
     * @return true if headers state the response is JSON, false otherwise
     */
    private static boolean isJson(String headers) {
        if (headers.contains("application/json")) {
            return true;
        }
        return false;
    }

    /**
     *
     */
    private static boolean isCard(String response) {
        return response.contains("RenderTemplate") || response.contains("RenderPlayerInfo");
    }
}