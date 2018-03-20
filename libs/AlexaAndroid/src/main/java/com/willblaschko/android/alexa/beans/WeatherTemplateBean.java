package com.willblaschko.android.alexa.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user001 on 2018-3-8.
 */

public class WeatherTemplateBean implements Parcelable {

    /**
     * directive : {"header":{"namespace":"TemplateRuntime","name":"RenderTemplate","messageId":"51861447-2974-4195-94bd-415d8f76e249","dialogRequestId":"dialogRequest-321"},"payload":{"type":"WeatherTemplate","token":"ca7054f3-c3ed-4255-9a71-42b6801f8057","lowTemperature":{"value":"30度","arrow":{"contentDescription":"Down arrow","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/down_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/down.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/down_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/down.png"}]}},"title":{"subTitle":"Thursday, March 15, 2018","mainTitle":"Beijing, China"},"currentWeatherIcon":{"contentDescription":"Sunny","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/sunny_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/sunny.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/sunny_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/sunny.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/sunny_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/sunny.png"}]},"weatherForecast":[{"highTemperature":"48度","date":"Mar 16","lowTemperature":"30度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Fri"},{"highTemperature":"50度","date":"Mar 17","lowTemperature":"36度","image":{"contentDescription":"Cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy.png"}]},"day":"Sat"},{"highTemperature":"54度","date":"Mar 18","lowTemperature":"36度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Sun"},{"highTemperature":"52度","date":"Mar 19","lowTemperature":"36度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Mon"},{"highTemperature":"48度","date":"Mar 20","lowTemperature":"34度","image":{"contentDescription":"Cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy.png"}]},"day":"Tue"},{"highTemperature":"54 度","date":"Mar 21","lowTemperature":"36度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Wed"},{"highTemperature":"63度","date":"Mar 22","lowTemperature":"39度","image":{"contentDescription":"Rainy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/rainy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/rainy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/rainy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/rainy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/rainy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/rainy.png"}]},"day":"Thu"}],"description":"Partly sunny","highTemperature":{"value":"55度","arrow":{"contentDescription":"Up arrow","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/up_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/up.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/up_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/up.png"}]}},"currentWeather":"55度"}}
     */

    private DirectiveBean directive;

    public DirectiveBean getDirective() {
        return directive;
    }

    public void setDirective(DirectiveBean directive) {
        this.directive = directive;
    }

    public static class DirectiveBean implements Parcelable {
        /**
         * header : {"namespace":"TemplateRuntime","name":"RenderTemplate","messageId":"51861447-2974-4195-94bd-415d8f76e249","dialogRequestId":"dialogRequest-321"}
         * payload : {"type":"WeatherTemplate","token":"ca7054f3-c3ed-4255-9a71-42b6801f8057","lowTemperature":{"value":"30度","arrow":{"contentDescription":"Down arrow","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/down_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/down.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/down_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/down.png"}]}},"title":{"subTitle":"Thursday, March 15, 2018","mainTitle":"Beijing, China"},"currentWeatherIcon":{"contentDescription":"Sunny","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/sunny_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/sunny.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/sunny_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/sunny.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/sunny_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/sunny.png"}]},"weatherForecast":[{"highTemperature":"48度","date":"Mar 16","lowTemperature":"30度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Fri"},{"highTemperature":"50度","date":"Mar 17","lowTemperature":"36度","image":{"contentDescription":"Cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy.png"}]},"day":"Sat"},{"highTemperature":"54度","date":"Mar 18","lowTemperature":"36度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Sun"},{"highTemperature":"52度","date":"Mar 19","lowTemperature":"36度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Mon"},{"highTemperature":"48度","date":"Mar 20","lowTemperature":"34度","image":{"contentDescription":"Cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy.png"}]},"day":"Tue"},{"highTemperature":"54 度","date":"Mar 21","lowTemperature":"36度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Wed"},{"highTemperature":"63度","date":"Mar 22","lowTemperature":"39度","image":{"contentDescription":"Rainy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/rainy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/rainy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/rainy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/rainy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/rainy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/rainy.png"}]},"day":"Thu"}],"description":"Partly sunny","highTemperature":{"value":"55度","arrow":{"contentDescription":"Up arrow","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/up_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/up.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/up_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/up.png"}]}},"currentWeather":"55度"}
         */

        private HeaderBean header;
        private PayloadBean payload;

        public HeaderBean getHeader() {
            return header;
        }

        public void setHeader(HeaderBean header) {
            this.header = header;
        }

        public PayloadBean getPayload() {
            return payload;
        }

        public void setPayload(PayloadBean payload) {
            this.payload = payload;
        }

        public static class HeaderBean implements Parcelable {
            /**
             * namespace : TemplateRuntime
             * name : RenderTemplate
             * messageId : 51861447-2974-4195-94bd-415d8f76e249
             * dialogRequestId : dialogRequest-321
             */

            private String namespace;
            private String name;
            private String messageId;
            private String dialogRequestId;

            public String getNamespace() {
                return namespace;
            }

            public void setNamespace(String namespace) {
                this.namespace = namespace;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMessageId() {
                return messageId;
            }

            public void setMessageId(String messageId) {
                this.messageId = messageId;
            }

            public String getDialogRequestId() {
                return dialogRequestId;
            }

            public void setDialogRequestId(String dialogRequestId) {
                this.dialogRequestId = dialogRequestId;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.namespace);
                dest.writeString(this.name);
                dest.writeString(this.messageId);
                dest.writeString(this.dialogRequestId);
            }

            public HeaderBean() {
            }

            protected HeaderBean(Parcel in) {
                this.namespace = in.readString();
                this.name = in.readString();
                this.messageId = in.readString();
                this.dialogRequestId = in.readString();
            }

            public static final Creator<HeaderBean> CREATOR = new Creator<HeaderBean>() {
                @Override
                public HeaderBean createFromParcel(Parcel source) {
                    return new HeaderBean(source);
                }

                @Override
                public HeaderBean[] newArray(int size) {
                    return new HeaderBean[size];
                }
            };
        }

        public static class PayloadBean implements Parcelable {
            /**
             * type : WeatherTemplate
             * token : ca7054f3-c3ed-4255-9a71-42b6801f8057
             * lowTemperature : {"value":"30度","arrow":{"contentDescription":"Down arrow","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/down_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/down.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/down_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/down.png"}]}}
             * title : {"subTitle":"Thursday, March 15, 2018","mainTitle":"Beijing, China"}
             * currentWeatherIcon : {"contentDescription":"Sunny","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/sunny_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/sunny.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/sunny_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/sunny.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/sunny_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/sunny.png"}]}
             * weatherForecast : [{"highTemperature":"48度","date":"Mar 16","lowTemperature":"30度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Fri"},{"highTemperature":"50度","date":"Mar 17","lowTemperature":"36度","image":{"contentDescription":"Cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy.png"}]},"day":"Sat"},{"highTemperature":"54度","date":"Mar 18","lowTemperature":"36度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Sun"},{"highTemperature":"52度","date":"Mar 19","lowTemperature":"36度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Mon"},{"highTemperature":"48度","date":"Mar 20","lowTemperature":"34度","image":{"contentDescription":"Cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/cloudy.png"}]},"day":"Tue"},{"highTemperature":"54 度","date":"Mar 21","lowTemperature":"36度","image":{"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]},"day":"Wed"},{"highTemperature":"63度","date":"Mar 22","lowTemperature":"39度","image":{"contentDescription":"Rainy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/rainy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/rainy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/rainy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/rainy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/rainy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/rainy.png"}]},"day":"Thu"}]
             * description : Partly sunny
             * highTemperature : {"value":"55度","arrow":{"contentDescription":"Up arrow","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/up_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/up.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/up_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/up.png"}]}}
             * currentWeather : 55度
             */

            private String type;
            private String token;
            private LowTemperatureBean lowTemperature;
            private TitleBean title;
            private CurrentWeatherIconBean currentWeatherIcon;
            private String description;
            private HighTemperatureBean highTemperature;
            private String currentWeather;
            private List<WeatherForecastBean> weatherForecast;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public LowTemperatureBean getLowTemperature() {
                return lowTemperature;
            }

            public void setLowTemperature(LowTemperatureBean lowTemperature) {
                this.lowTemperature = lowTemperature;
            }

            public TitleBean getTitle() {
                return title;
            }

            public void setTitle(TitleBean title) {
                this.title = title;
            }

            public CurrentWeatherIconBean getCurrentWeatherIcon() {
                return currentWeatherIcon;
            }

            public void setCurrentWeatherIcon(CurrentWeatherIconBean currentWeatherIcon) {
                this.currentWeatherIcon = currentWeatherIcon;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public HighTemperatureBean getHighTemperature() {
                return highTemperature;
            }

            public void setHighTemperature(HighTemperatureBean highTemperature) {
                this.highTemperature = highTemperature;
            }

            public String getCurrentWeather() {
                return currentWeather;
            }

            public void setCurrentWeather(String currentWeather) {
                this.currentWeather = currentWeather;
            }

            public List<WeatherForecastBean> getWeatherForecast() {
                return weatherForecast;
            }

            public void setWeatherForecast(List<WeatherForecastBean> weatherForecast) {
                this.weatherForecast = weatherForecast;
            }

            public static class LowTemperatureBean implements Parcelable {
                /**
                 * value : 30度
                 * arrow : {"contentDescription":"Down arrow","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/down_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/down.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/down_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/down.png"}]}
                 */

                private String value;
                private ArrowBean arrow;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public ArrowBean getArrow() {
                    return arrow;
                }

                public void setArrow(ArrowBean arrow) {
                    this.arrow = arrow;
                }

                public static class ArrowBean implements Parcelable {
                    /**
                     * contentDescription : Down arrow
                     * sources : [{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/down_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/down.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/down_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/down.png"}]
                     */

                    private String contentDescription;
                    private List<SourcesBean> sources;

                    public String getContentDescription() {
                        return contentDescription;
                    }

                    public void setContentDescription(String contentDescription) {
                        this.contentDescription = contentDescription;
                    }

                    public List<SourcesBean> getSources() {
                        return sources;
                    }

                    public void setSources(List<SourcesBean> sources) {
                        this.sources = sources;
                    }

                    public static class SourcesBean implements Parcelable {
                        /**
                         * widthPixels : 88
                         * darkBackgroundUrl : https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down_bb.png
                         * size : SMALL
                         * heightPixels : 80
                         * url : https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/down.png
                         */

                        private int widthPixels;
                        private String darkBackgroundUrl;
                        private String size;
                        private int heightPixels;
                        private String url;

                        public int getWidthPixels() {
                            return widthPixels;
                        }

                        public void setWidthPixels(int widthPixels) {
                            this.widthPixels = widthPixels;
                        }

                        public String getDarkBackgroundUrl() {
                            return darkBackgroundUrl;
                        }

                        public void setDarkBackgroundUrl(String darkBackgroundUrl) {
                            this.darkBackgroundUrl = darkBackgroundUrl;
                        }

                        public String getSize() {
                            return size;
                        }

                        public void setSize(String size) {
                            this.size = size;
                        }

                        public int getHeightPixels() {
                            return heightPixels;
                        }

                        public void setHeightPixels(int heightPixels) {
                            this.heightPixels = heightPixels;
                        }

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {
                            dest.writeInt(this.widthPixels);
                            dest.writeString(this.darkBackgroundUrl);
                            dest.writeString(this.size);
                            dest.writeInt(this.heightPixels);
                            dest.writeString(this.url);
                        }

                        public SourcesBean() {
                        }

                        protected SourcesBean(Parcel in) {
                            this.widthPixels = in.readInt();
                            this.darkBackgroundUrl = in.readString();
                            this.size = in.readString();
                            this.heightPixels = in.readInt();
                            this.url = in.readString();
                        }

                        public static final Creator<SourcesBean> CREATOR = new Creator<SourcesBean>() {
                            @Override
                            public SourcesBean createFromParcel(Parcel source) {
                                return new SourcesBean(source);
                            }

                            @Override
                            public SourcesBean[] newArray(int size) {
                                return new SourcesBean[size];
                            }
                        };
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(this.contentDescription);
                        dest.writeList(this.sources);
                    }

                    public ArrowBean() {
                    }

                    protected ArrowBean(Parcel in) {
                        this.contentDescription = in.readString();
                        this.sources = new ArrayList<SourcesBean>();
                        in.readList(this.sources, SourcesBean.class.getClassLoader());
                    }

                    public static final Creator<ArrowBean> CREATOR = new Creator<ArrowBean>() {
                        @Override
                        public ArrowBean createFromParcel(Parcel source) {
                            return new ArrowBean(source);
                        }

                        @Override
                        public ArrowBean[] newArray(int size) {
                            return new ArrowBean[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.value);
                    dest.writeParcelable(this.arrow, flags);
                }

                public LowTemperatureBean() {
                }

                protected LowTemperatureBean(Parcel in) {
                    this.value = in.readString();
                    this.arrow = in.readParcelable(ArrowBean.class.getClassLoader());
                }

                public static final Creator<LowTemperatureBean> CREATOR = new Creator<LowTemperatureBean>() {
                    @Override
                    public LowTemperatureBean createFromParcel(Parcel source) {
                        return new LowTemperatureBean(source);
                    }

                    @Override
                    public LowTemperatureBean[] newArray(int size) {
                        return new LowTemperatureBean[size];
                    }
                };
            }

            public static class TitleBean implements Parcelable {
                /**
                 * subTitle : Thursday, March 15, 2018
                 * mainTitle : Beijing, China
                 */

                private String subTitle;
                private String mainTitle;

                public String getSubTitle() {
                    return subTitle;
                }

                public void setSubTitle(String subTitle) {
                    this.subTitle = subTitle;
                }

                public String getMainTitle() {
                    return mainTitle;
                }

                public void setMainTitle(String mainTitle) {
                    this.mainTitle = mainTitle;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.subTitle);
                    dest.writeString(this.mainTitle);
                }

                public TitleBean() {
                }

                protected TitleBean(Parcel in) {
                    this.subTitle = in.readString();
                    this.mainTitle = in.readString();
                }

                public static final Creator<TitleBean> CREATOR = new Creator<TitleBean>() {
                    @Override
                    public TitleBean createFromParcel(Parcel source) {
                        return new TitleBean(source);
                    }

                    @Override
                    public TitleBean[] newArray(int size) {
                        return new TitleBean[size];
                    }
                };
            }

            public static class CurrentWeatherIconBean implements Parcelable {
                /**
                 * contentDescription : Sunny
                 * sources : [{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/sunny_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/sunny.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/sunny_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/sunny.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/sunny_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/sunny.png"}]
                 */

                private String contentDescription;
                private List<SourcesBeanX> sources;

                public String getContentDescription() {
                    return contentDescription;
                }

                public void setContentDescription(String contentDescription) {
                    this.contentDescription = contentDescription;
                }

                public List<SourcesBeanX> getSources() {
                    return sources;
                }

                public void setSources(List<SourcesBeanX> sources) {
                    this.sources = sources;
                }

                public static class SourcesBeanX implements Parcelable {
                    /**
                     * widthPixels : 88
                     * darkBackgroundUrl : https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/sunny_bb.png
                     * size : SMALL
                     * heightPixels : 80
                     * url : https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/sunny.png
                     */

                    private int widthPixels;
                    private String darkBackgroundUrl;
                    private String size;
                    private int heightPixels;
                    private String url;

                    public int getWidthPixels() {
                        return widthPixels;
                    }

                    public void setWidthPixels(int widthPixels) {
                        this.widthPixels = widthPixels;
                    }

                    public String getDarkBackgroundUrl() {
                        return darkBackgroundUrl;
                    }

                    public void setDarkBackgroundUrl(String darkBackgroundUrl) {
                        this.darkBackgroundUrl = darkBackgroundUrl;
                    }

                    public String getSize() {
                        return size;
                    }

                    public void setSize(String size) {
                        this.size = size;
                    }

                    public int getHeightPixels() {
                        return heightPixels;
                    }

                    public void setHeightPixels(int heightPixels) {
                        this.heightPixels = heightPixels;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(this.widthPixels);
                        dest.writeString(this.darkBackgroundUrl);
                        dest.writeString(this.size);
                        dest.writeInt(this.heightPixels);
                        dest.writeString(this.url);
                    }

                    public SourcesBeanX() {
                    }

                    protected SourcesBeanX(Parcel in) {
                        this.widthPixels = in.readInt();
                        this.darkBackgroundUrl = in.readString();
                        this.size = in.readString();
                        this.heightPixels = in.readInt();
                        this.url = in.readString();
                    }

                    public static final Creator<SourcesBeanX> CREATOR = new Creator<SourcesBeanX>() {
                        @Override
                        public SourcesBeanX createFromParcel(Parcel source) {
                            return new SourcesBeanX(source);
                        }

                        @Override
                        public SourcesBeanX[] newArray(int size) {
                            return new SourcesBeanX[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.contentDescription);
                    dest.writeList(this.sources);
                }

                public CurrentWeatherIconBean() {
                }

                protected CurrentWeatherIconBean(Parcel in) {
                    this.contentDescription = in.readString();
                    this.sources = new ArrayList<SourcesBeanX>();
                    in.readList(this.sources, SourcesBeanX.class.getClassLoader());
                }

                public static final Creator<CurrentWeatherIconBean> CREATOR = new Creator<CurrentWeatherIconBean>() {
                    @Override
                    public CurrentWeatherIconBean createFromParcel(Parcel source) {
                        return new CurrentWeatherIconBean(source);
                    }

                    @Override
                    public CurrentWeatherIconBean[] newArray(int size) {
                        return new CurrentWeatherIconBean[size];
                    }
                };
            }

            public static class HighTemperatureBean implements Parcelable {
                /**
                 * value : 55度
                 * arrow : {"contentDescription":"Up arrow","sources":[{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/up_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/up.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/up_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/up.png"}]}
                 */

                private String value;
                private ArrowBeanX arrow;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public ArrowBeanX getArrow() {
                    return arrow;
                }

                public void setArrow(ArrowBeanX arrow) {
                    this.arrow = arrow;
                }

                public static class ArrowBeanX implements Parcelable {
                    /**
                     * contentDescription : Up arrow
                     * sources : [{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up_bb.png","size":"SMALL","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up.png"},{"widthPixels":256,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/up_bb.png","size":"MEDIUM","heightPixels":233,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/currentWeatherIcon/up.png"},{"widthPixels":305,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/up_bb.png","size":"LARGE","heightPixels":278,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/up.png"}]
                     */

                    private String contentDescription;
                    private List<SourcesBeanXX> sources;

                    public String getContentDescription() {
                        return contentDescription;
                    }

                    public void setContentDescription(String contentDescription) {
                        this.contentDescription = contentDescription;
                    }

                    public List<SourcesBeanXX> getSources() {
                        return sources;
                    }

                    public void setSources(List<SourcesBeanXX> sources) {
                        this.sources = sources;
                    }

                    public static class SourcesBeanXX implements Parcelable {
                        /**
                         * widthPixels : 88
                         * darkBackgroundUrl : https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up_bb.png
                         * size : SMALL
                         * heightPixels : 80
                         * url : https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/currentWeatherIcon/up.png
                         */

                        private int widthPixels;
                        private String darkBackgroundUrl;
                        private String size;
                        private int heightPixels;
                        private String url;

                        public int getWidthPixels() {
                            return widthPixels;
                        }

                        public void setWidthPixels(int widthPixels) {
                            this.widthPixels = widthPixels;
                        }

                        public String getDarkBackgroundUrl() {
                            return darkBackgroundUrl;
                        }

                        public void setDarkBackgroundUrl(String darkBackgroundUrl) {
                            this.darkBackgroundUrl = darkBackgroundUrl;
                        }

                        public String getSize() {
                            return size;
                        }

                        public void setSize(String size) {
                            this.size = size;
                        }

                        public int getHeightPixels() {
                            return heightPixels;
                        }

                        public void setHeightPixels(int heightPixels) {
                            this.heightPixels = heightPixels;
                        }

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {
                            dest.writeInt(this.widthPixels);
                            dest.writeString(this.darkBackgroundUrl);
                            dest.writeString(this.size);
                            dest.writeInt(this.heightPixels);
                            dest.writeString(this.url);
                        }

                        public SourcesBeanXX() {
                        }

                        protected SourcesBeanXX(Parcel in) {
                            this.widthPixels = in.readInt();
                            this.darkBackgroundUrl = in.readString();
                            this.size = in.readString();
                            this.heightPixels = in.readInt();
                            this.url = in.readString();
                        }

                        public static final Creator<SourcesBeanXX> CREATOR = new Creator<SourcesBeanXX>() {
                            @Override
                            public SourcesBeanXX createFromParcel(Parcel source) {
                                return new SourcesBeanXX(source);
                            }

                            @Override
                            public SourcesBeanXX[] newArray(int size) {
                                return new SourcesBeanXX[size];
                            }
                        };
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(this.contentDescription);
                        dest.writeList(this.sources);
                    }

                    public ArrowBeanX() {
                    }

                    protected ArrowBeanX(Parcel in) {
                        this.contentDescription = in.readString();
                        this.sources = new ArrayList<SourcesBeanXX>();
                        in.readList(this.sources, SourcesBeanXX.class.getClassLoader());
                    }

                    public static final Creator<ArrowBeanX> CREATOR = new Creator<ArrowBeanX>() {
                        @Override
                        public ArrowBeanX createFromParcel(Parcel source) {
                            return new ArrowBeanX(source);
                        }

                        @Override
                        public ArrowBeanX[] newArray(int size) {
                            return new ArrowBeanX[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.value);
                    dest.writeParcelable(this.arrow, flags);
                }

                public HighTemperatureBean() {
                }

                protected HighTemperatureBean(Parcel in) {
                    this.value = in.readString();
                    this.arrow = in.readParcelable(ArrowBeanX.class.getClassLoader());
                }

                public static final Creator<HighTemperatureBean> CREATOR = new Creator<HighTemperatureBean>() {
                    @Override
                    public HighTemperatureBean createFromParcel(Parcel source) {
                        return new HighTemperatureBean(source);
                    }

                    @Override
                    public HighTemperatureBean[] newArray(int size) {
                        return new HighTemperatureBean[size];
                    }
                };
            }

            public static class WeatherForecastBean implements Parcelable {
                /**
                 * highTemperature : 48度
                 * date : Mar 16
                 * lowTemperature : 30度
                 * image : {"contentDescription":"Partly cloudy","sources":[{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]}
                 * day : Fri
                 */

                private String highTemperature;
                private String date;
                private String lowTemperature;
                private ImageBean image;
                private String day;

                public String getHighTemperature() {
                    return highTemperature;
                }

                public void setHighTemperature(String highTemperature) {
                    this.highTemperature = highTemperature;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getLowTemperature() {
                    return lowTemperature;
                }

                public void setLowTemperature(String lowTemperature) {
                    this.lowTemperature = lowTemperature;
                }

                public ImageBean getImage() {
                    return image;
                }

                public void setImage(ImageBean image) {
                    this.image = image;
                }

                public String getDay() {
                    return day;
                }

                public void setDay(String day) {
                    this.day = day;
                }

                public static class ImageBean implements Parcelable {
                    /**
                     * contentDescription : Partly cloudy
                     * sources : [{"widthPixels":44,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png","size":"SMALL","heightPixels":40,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png"},{"widthPixels":88,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy_bb.png","size":"MEDIUM","heightPixels":80,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/medium/weatherForecast/partly_cloudy.png"},{"widthPixels":132,"darkBackgroundUrl":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy_bb.png","size":"LARGE","heightPixels":123,"url":"https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/weatherForecast/partly_cloudy.png"}]
                     */

                    private String contentDescription;
                    private List<SourcesBeanXXX> sources;

                    public String getContentDescription() {
                        return contentDescription;
                    }

                    public void setContentDescription(String contentDescription) {
                        this.contentDescription = contentDescription;
                    }

                    public List<SourcesBeanXXX> getSources() {
                        return sources;
                    }

                    public void setSources(List<SourcesBeanXXX> sources) {
                        this.sources = sources;
                    }

                    public static class SourcesBeanXXX implements Parcelable {
                        /**
                         * widthPixels : 44
                         * darkBackgroundUrl : https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy_bb.png
                         * size : SMALL
                         * heightPixels : 40
                         * url : https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/small/weatherForecast/partly_cloudy.png
                         */

                        private int widthPixels;
                        private String darkBackgroundUrl;
                        private String size;
                        private int heightPixels;
                        private String url;

                        public int getWidthPixels() {
                            return widthPixels;
                        }

                        public void setWidthPixels(int widthPixels) {
                            this.widthPixels = widthPixels;
                        }

                        public String getDarkBackgroundUrl() {
                            return darkBackgroundUrl;
                        }

                        public void setDarkBackgroundUrl(String darkBackgroundUrl) {
                            this.darkBackgroundUrl = darkBackgroundUrl;
                        }

                        public String getSize() {
                            return size;
                        }

                        public void setSize(String size) {
                            this.size = size;
                        }

                        public int getHeightPixels() {
                            return heightPixels;
                        }

                        public void setHeightPixels(int heightPixels) {
                            this.heightPixels = heightPixels;
                        }

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {
                            dest.writeInt(this.widthPixels);
                            dest.writeString(this.darkBackgroundUrl);
                            dest.writeString(this.size);
                            dest.writeInt(this.heightPixels);
                            dest.writeString(this.url);
                        }

                        public SourcesBeanXXX() {
                        }

                        protected SourcesBeanXXX(Parcel in) {
                            this.widthPixels = in.readInt();
                            this.darkBackgroundUrl = in.readString();
                            this.size = in.readString();
                            this.heightPixels = in.readInt();
                            this.url = in.readString();
                        }

                        public static final Creator<SourcesBeanXXX> CREATOR = new Creator<SourcesBeanXXX>() {
                            @Override
                            public SourcesBeanXXX createFromParcel(Parcel source) {
                                return new SourcesBeanXXX(source);
                            }

                            @Override
                            public SourcesBeanXXX[] newArray(int size) {
                                return new SourcesBeanXXX[size];
                            }
                        };
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(this.contentDescription);
                        dest.writeList(this.sources);
                    }

                    public ImageBean() {
                    }

                    protected ImageBean(Parcel in) {
                        this.contentDescription = in.readString();
                        this.sources = new ArrayList<SourcesBeanXXX>();
                        in.readList(this.sources, SourcesBeanXXX.class.getClassLoader());
                    }

                    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
                        @Override
                        public ImageBean createFromParcel(Parcel source) {
                            return new ImageBean(source);
                        }

                        @Override
                        public ImageBean[] newArray(int size) {
                            return new ImageBean[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.highTemperature);
                    dest.writeString(this.date);
                    dest.writeString(this.lowTemperature);
                    dest.writeParcelable(this.image, flags);
                    dest.writeString(this.day);
                }

                public WeatherForecastBean() {
                }

                protected WeatherForecastBean(Parcel in) {
                    this.highTemperature = in.readString();
                    this.date = in.readString();
                    this.lowTemperature = in.readString();
                    this.image = in.readParcelable(ImageBean.class.getClassLoader());
                    this.day = in.readString();
                }

                public static final Creator<WeatherForecastBean> CREATOR = new Creator<WeatherForecastBean>() {
                    @Override
                    public WeatherForecastBean createFromParcel(Parcel source) {
                        return new WeatherForecastBean(source);
                    }

                    @Override
                    public WeatherForecastBean[] newArray(int size) {
                        return new WeatherForecastBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.type);
                dest.writeString(this.token);
                dest.writeParcelable(this.lowTemperature, flags);
                dest.writeParcelable(this.title, flags);
                dest.writeParcelable(this.currentWeatherIcon, flags);
                dest.writeString(this.description);
                dest.writeParcelable(this.highTemperature, flags);
                dest.writeString(this.currentWeather);
                dest.writeList(this.weatherForecast);
            }

            public PayloadBean() {
            }

            protected PayloadBean(Parcel in) {
                this.type = in.readString();
                this.token = in.readString();
                this.lowTemperature = in.readParcelable(LowTemperatureBean.class.getClassLoader());
                this.title = in.readParcelable(TitleBean.class.getClassLoader());
                this.currentWeatherIcon = in.readParcelable(CurrentWeatherIconBean.class.getClassLoader());
                this.description = in.readString();
                this.highTemperature = in.readParcelable(HighTemperatureBean.class.getClassLoader());
                this.currentWeather = in.readString();
                this.weatherForecast = new ArrayList<WeatherForecastBean>();
                in.readList(this.weatherForecast, WeatherForecastBean.class.getClassLoader());
            }

            public static final Creator<PayloadBean> CREATOR = new Creator<PayloadBean>() {
                @Override
                public PayloadBean createFromParcel(Parcel source) {
                    return new PayloadBean(source);
                }

                @Override
                public PayloadBean[] newArray(int size) {
                    return new PayloadBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.header, flags);
            dest.writeParcelable(this.payload, flags);
        }

        public DirectiveBean() {
        }

        protected DirectiveBean(Parcel in) {
            this.header = in.readParcelable(HeaderBean.class.getClassLoader());
            this.payload = in.readParcelable(PayloadBean.class.getClassLoader());
        }

        public static final Creator<DirectiveBean> CREATOR = new Creator<DirectiveBean>() {
            @Override
            public DirectiveBean createFromParcel(Parcel source) {
                return new DirectiveBean(source);
            }

            @Override
            public DirectiveBean[] newArray(int size) {
                return new DirectiveBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.directive, flags);
    }

    public WeatherTemplateBean() {
    }

    protected WeatherTemplateBean(Parcel in) {
        this.directive = in.readParcelable(DirectiveBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<WeatherTemplateBean> CREATOR = new Parcelable.Creator<WeatherTemplateBean>() {
        @Override
        public WeatherTemplateBean createFromParcel(Parcel source) {
            return new WeatherTemplateBean(source);
        }

        @Override
        public WeatherTemplateBean[] newArray(int size) {
            return new WeatherTemplateBean[size];
        }
    };
}
