package com.willblaschko.android.alexa.data;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.willblaschko.android.alexa.audioplayer.AlexaAudioPlayer;
import com.willblaschko.android.alexa.beans.AlertContextBean;
import com.willblaschko.android.alexa.interfaces.AvsItem;
import com.willblaschko.android.alexa.interfaces.audioplayer.AvsPlayRemoteItem;
import com.willblaschko.android.alexa.interfaces.speechsynthesizer.AvsSpeakItem;
import com.willblaschko.android.alexa.system.AndroidSystemHandler;
import com.willblaschko.android.alexa.utility.AlertUtil;

import java.util.ArrayList;
import java.util.List;

import static com.willblaschko.android.alexa.utility.Util.getUuid;

/**
 * A catch-all Event to classify return responses from the Amazon Alexa v20160207 API
 * Will handle calls to:
 * <a href="https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/speechrecognizer">Speech Recognizer</a>
 * <a href="https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/alerts">Alerts</a>
 * <a href="https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/audioplayer">Audio Player</a>
 * <a href="https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/playbackcontroller">Playback Controller</a>
 * <a href="https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/speaker">Speaker</a>
 * <a href="https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/speechsynthesizer">Speech Synthesizer</a>
 * <a href="https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/system">System</a>
 *
 * @author wblaschko on 5/6/16.
 */
public class Event {

    Header header;
    Payload payload;
    List<Event> context;
    private static final String TAG = "Event";

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }


    public static class Header{

        String namespace;
        String name;
        String messageId;
        String dialogRequestId;

        public String getNamespace() {
            return namespace;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getMessageId() {
            return messageId;
        }

        public String getDialogRequestId() {
            return dialogRequestId;
        }

        public void setDialogRequestId(String dialogRequestId) {
            this.dialogRequestId = dialogRequestId;
        }
    }

    public static class Payload{
        String token;
        String profile;
        String format;
        String playerActivity;
        Boolean muted;
        Long volume;
        Long offsetInMilliseconds;

        List<AlertContextBean> allAlerts;
        List<AlertContextBean> activeAlerts;

        public String getProfile() {
            return profile;
        }

        public String getFormat() {
            return format;
        }

    }

    public static class EventWrapper{
        Event event;
        List<Event> context = new ArrayList<>();

        public Event getEvent() {
            return event;
        }

        public List<Event> getContext() {
            return context;
        }

        public String toJson(){
            return new Gson().toJson(this)+"\n";
//            return ""
        }
    }

    public static class Builder{
        Event event = new Event();
        Payload payload = new Payload();
        Header header = new Header();
        List<Event> context = new ArrayList<>();

        public Builder(){
            event.setPayload(payload);
            event.setHeader(header);
        }

        public EventWrapper build(){
            EventWrapper wrapper = new EventWrapper();
            wrapper.event = event;
            if (context != null && !context.isEmpty() && !(context.size() == 1 && context.get(0) == null)) {
                wrapper.context = context;
            }

            return wrapper;
        }

        public String toJson(){
            Log.d(TAG, "toJson: " + build().toJson());
            return build().toJson();
        }

        public Builder setContext(List<Event> context) {
            if (context == null) {
                return this;
            }

            this.context = context;
            return this;
        }

        public Builder setHeaderNamespace(String namespace){
            header.namespace = namespace;
            return this;
        }

        public Builder setHeaderName(String name){
            header.name = name;
            return this;
        }

        public Builder setHeaderMessageId(String messageId){
            header.messageId = messageId;
            return this;
        }

        public Builder setHeaderDialogRequestId(String dialogRequestId){
            header.dialogRequestId = dialogRequestId;
            return this;
        }

        public Builder setPayloadProfile(String profile){
            payload.profile = profile;
            return this;
        }
        public Builder setPayloadFormat(String format){
            payload.format = format;
            return this;
        }

        public Builder setPayloadplayerActivity(String playerActivity){
            payload.playerActivity = playerActivity;
            return this;
        }

        public Builder setPayloadMuted(boolean muted){
            payload.muted = muted;
            return this;
        }

        public Builder setPayloadVolume(long volume){
            payload.volume = volume;
            return this;
        }
        public Builder setPayloadToken(String token){
            payload.token = token;
            return this;
        }
        public Builder setPlayloadOffsetInMilliseconds(long offsetInMilliseconds){
            payload.offsetInMilliseconds = offsetInMilliseconds;
            return this;
        }

        private Builder setPayloadAllAlerts(List<AlertContextBean> allAlerts) {
            payload.allAlerts = allAlerts;
            String s = new Gson().toJson(allAlerts);
            String s1 = JSON.toJSONString(allAlerts);
            Log.d(TAG, "setPayloadAllAlerts: ---s1 >" + s1);
            return this;
        }

        private Builder setPayloadActiveAlerts(List<AlertContextBean> actviceAlerts) {
            payload.activeAlerts = actviceAlerts;
            return this;
        }

        public Event getEvent() {
            return event;
        }
    }

    public static String getSpeechRecognizerEvent(AvsItem item) {
        Builder builder = new Builder();
        builder.setHeaderNamespace("SpeechRecognizer")
                .setContext(getContext())
                .setHeaderName("Recognize")
                .setHeaderMessageId(getUuid())
                .setHeaderDialogRequestId("dialogRequest-321")
                .setPayloadFormat("AUDIO_L16_RATE_16000_CHANNELS_1")
                .setPayloadProfile("CLOSE_TALK");

        Log.d(TAG,"getSpeechRecognizerEvent:"+builder.toJson());
        Log.d(TAG, "getSpeechRecognizerEvent: ----item is " + item);
        // TODO:add alert context
        /* Builder alertBuilder = new Builder();
       Event alertContextEvent = alertBuilder
                .setHeaderNamespace("Alerts")
                .setHeaderName("AlertsState")
                .setPayloadToken(i)*/
        return builder.toJson();
    }

    public static String getVolumeChangedEvent(long volume, boolean isMute){
        Builder builder = new Builder();
        builder.setHeaderNamespace("Speaker")
                .setHeaderName("VolumeChanged")
                .setHeaderMessageId(getUuid())
                .setPayloadVolume(volume)
                .setPayloadMuted(isMute);
        return builder.toJson();
    }

    public static String getMuteEvent( long volume,boolean isMute) {
        Builder builder = new Builder();
        builder.setHeaderNamespace("Speaker")
                .setHeaderName("MuteChanged")
                .setHeaderMessageId(getUuid())
                .setPayloadVolume(volume)
                .setPayloadMuted(isMute);
        return builder.toJson();
    }

    public static String getExpectSpeechTimedOutEvent(){
        Builder builder = new Builder();
        builder.setHeaderNamespace("SpeechRecognizer")
                .setHeaderName("ExpectSpeechTimedOut")
                .setHeaderMessageId(getUuid());
        return builder.toJson();
    }

    public static String getPlaybackNearlyFinishedEvent(String token, long offsetInMilliseconds) {
        Builder builder = new Builder();
        AlexaAudioPlayer AudioPlayer = AlexaAudioPlayer.getInstance(null);

        builder.setHeaderNamespace("AudioPlayer")
                .setHeaderName("PlaybackNearlyFinished")
                .setHeaderMessageId(getUuid())
                .setPayloadToken(token)
                .setPlayloadOffsetInMilliseconds(AudioPlayer != null ? AudioPlayer.getCurrentPosition() : offsetInMilliseconds);
        return builder.toJson();
    }

    public static String getPlaybackControllerPlayCommandIssued(){
        Builder builder = new Builder();
        builder.setHeaderNamespace("PlaybackController")
                .setContext(getContext())
                .setHeaderName("PlayCommandIssued")
                .setHeaderMessageId(getUuid());
        return builder.toJson();
    }

    public static String getPlaybackControllerPauseCommandIssued(){
        Builder builder = new Builder();
        builder.setHeaderNamespace("PlaybackController")
                .setContext(getContext())
                .setHeaderName("PauseCommandIssued")
                .setHeaderMessageId(getUuid());
        return builder.toJson();
    }

    public static String getPlaybackControllerNextCommandIssued(){
        Builder builder = new Builder();
        builder.setHeaderNamespace("PlaybackController")
                .setContext(getContext())
                .setHeaderName("NextCommandIssued")
                .setHeaderMessageId(getUuid());
        return builder.toJson();
    }

    public static String getPlaybackControllerPreviousCommandIssued(){

        Builder builder = new Builder();
        builder.setHeaderNamespace("PlaybackController")
                .setContext(getContext())
                .setHeaderName("PreviousCommandIssued")
                .setHeaderMessageId(getUuid());
        return builder.toJson();
    }

    public static String getSetAlertSucceededEvent(String token) {
        return getAlertEvent(token, "SetAlertSucceeded");
    }

    public static String getSetAlertFailedEvent(String token) {
        return getAlertEvent(token, "SetAlertFailed");
    }

    public static String getDeleteAlertSucceededEvent(String token) {
        return getAlertEvent(token, "DeleteAlertSucceeded");
    }

    public static String getDeleteAlertFailedEvent(String token) {
        return getAlertEvent(token, "DeleteAlertFailed");
    }

    public static String getAlertStartedEvent(String token) {
        return getAlertEvent(token, "AlertStarted");
    }

    public static String getAlertStoppedEvent(String token) {
        return getAlertEvent(token, "AlertStopped");
    }

    public static String getAlertEnteredForegroundEvent(String token) {
        return getAlertEvent(token, "AlertEnteredForeground");
    }

    public static String getAlertEnteredBackgroundEvent(String token) {
        return getAlertEvent(token, "AlertEnteredBackground");
    }

    private static String getAlertEvent(String token, String type) {
        Builder builder = new Builder();
        builder.setHeaderNamespace("Alerts")
                .setHeaderName(type)
                .setHeaderMessageId(getUuid())
                .setPayloadToken(token);
        return builder.toJson();
    }

    public static String getSpeechStartedEvent(String token){
        Builder builder = new Builder();
        builder.setHeaderNamespace("SpeechSynthesizer")
                .setHeaderName("SpeechStarted")
                .setHeaderMessageId(getUuid())
                .setPayloadToken(token);
        return builder.toJson();
    }

    public static String getSpeechFinishedEvent(String token){
        Builder builder = new Builder();
        builder.setHeaderNamespace("SpeechSynthesizer")
                .setHeaderName("SpeechFinished")
                .setHeaderMessageId(getUuid())
                .setPayloadToken(token);
        return builder.toJson();
    }


    public static String getPlaybackStartedEvent(String token, long offset){
        Builder builder = new Builder();
        AlexaAudioPlayer AudioPlayer = AlexaAudioPlayer.getInstance(null);
        builder.setHeaderNamespace("AudioPlayer")
                .setHeaderName("PlaybackStarted")
                .setPlayloadOffsetInMilliseconds(AudioPlayer != null ? AudioPlayer.getCurrentPosition():offset)
                .setHeaderMessageId(getUuid())
                .setPayloadToken(token);
        return builder.toJson();
    }

    public static String getPlaybackStopEvent(String token, long offset) {
        Builder builder = new Builder();
        AlexaAudioPlayer AudioPlayer = AlexaAudioPlayer.getInstance(null);
        builder.setHeaderNamespace("AudioPlayer")
                .setHeaderName("PlaybackStopped")
                .setPlayloadOffsetInMilliseconds(AudioPlayer != null ? AudioPlayer.getCurrentPosition():offset)
                .setHeaderMessageId(getUuid())
                .setPayloadToken(token);
        return builder.toJson();
    }


    public static String getPlaybackFinishedEvent(String token){
        Builder builder = new Builder();
        AlexaAudioPlayer AudioPlayer = AlexaAudioPlayer.getInstance(null);
        long offset = 0;

        if( AudioPlayer != null && AudioPlayer.getCurrentItem() instanceof AvsPlayRemoteItem){
            offset =((AvsPlayRemoteItem) (AudioPlayer.getCurrentItem())).getStartOffset();
        }
        builder.setHeaderNamespace("AudioPlayer")
                .setHeaderName("PlaybackFinished")
                .setPlayloadOffsetInMilliseconds(offset)
                .setHeaderMessageId(getUuid())
                .setPayloadToken(token);
        return builder.toJson();
    }


    public static String getSynchronizeStateEvent(){
        Builder builder = new Builder();
        builder.setHeaderNamespace("System")
                .setHeaderName("SynchronizeState")
                .setContext(getContext())
                .setHeaderMessageId(getUuid());
        return builder.toJson();
    }

    private static List<Event> getContext() {
        List<Event> context = new ArrayList<>();
        AlexaAudioPlayer AudioPlayer = AlexaAudioPlayer.getInstance(null);
        if (AudioPlayer != null) {
            AvsPlayRemoteItem playRemoteItem = AudioPlayer.getLastAvsPlayRemoteItem();
            AvsSpeakItem speakItem = AudioPlayer.getLastAvsSpeakItem();
            Builder speechSynthesizerPlaybackStateBuilder = new Builder();
            Builder AudioPlayerPlaybackStateBuilder =new Builder();

            Event event;
            if( playRemoteItem != null ){
                event = AudioPlayerPlaybackStateBuilder.setHeaderNamespace("AudioPlayer")
                        .setHeaderName("PlaybackState")
                        .setPayloadToken(playRemoteItem.getToken())
                        .setPlayloadOffsetInMilliseconds(playRemoteItem.getStartOffset())
                        .setPayloadplayerActivity(playRemoteItem.getPlayerAcivity())
                        .getEvent();
                context.add(event);
            } /*else {
                event = AudioPlayerPlaybackStateBuilder.setHeaderNamespace("AudioPlayer")
                        .setHeaderName("PlaybackState")
                        .setPlayloadOffsetInMilliseconds(0)
                        .setPayloadplayerActivity(AvsPlayRemoteItem.PLAYER_ACTIVITY_IDLE)
                        .getEvent();
                context.add(event);
            }*/

            if( speakItem != null){
                event = speechSynthesizerPlaybackStateBuilder.setHeaderNamespace("SpeechSynthesizer")
                        .setHeaderName("SpeechState")
                        .setPayloadToken(speakItem.getToken())
                        .setPlayloadOffsetInMilliseconds(speakItem.getOffset())
                        .setPayloadplayerActivity(speakItem.getPlayerAcivity())
                        .getEvent();
                context.add(event);

            }/*else{
                event = speechSynthesizerPlaybackStateBuilder.setHeaderNamespace("SpeechSynthesizer")
                        .setHeaderName("SpeechState")
                        .setPlayloadOffsetInMilliseconds( 0 )
                        .setPayloadplayerActivity(AvsSpeakItem.PLAYER_ACTIVITY_FINISHED)
                        .getEvent();
                context.add(event);
            }*/
        }

        AndroidSystemHandler sysHandle = AndroidSystemHandler.getInstance(null);
        if (sysHandle != null) {
            Builder speakerStateBuilder = new Builder();
            Event event = speakerStateBuilder.setHeaderNamespace("Speaker")
                    .setHeaderName("VolumeState")
                    .setPayloadVolume(sysHandle.getVolume())
                    .setPayloadMuted(sysHandle.isMute())
                    .getEvent();
            context.add(event);

            Builder alertBuilder = new Builder();
            Event alertContextEvent = alertBuilder
                    .setHeaderNamespace("Alerts")
                    .setHeaderName("AlertsState")
                    .setPayloadAllAlerts(AlertUtil.getAllAlerts())
                    .setPayloadActiveAlerts(AlertUtil.getActiveAlerts())
                    .getEvent();
            context.add(alertContextEvent);
        }
        return context;
    }
}


