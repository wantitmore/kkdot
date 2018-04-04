package com.willblaschko.android.alexa.beans;

import java.util.List;

/**
 * Created by user001 on 2018-4-3.
 */

public class AlertBean {

    /**
     * directive : {"header":{"namespace":"Alerts","name":"SetAlert","messageId":"af70ad5c-b01d-4015-bd44-668b73124eff"},"payload":{"type":"ALARM","scheduledTime":"2018-04-03T16:00:00+0000","assets":[{"assetId":"3af1a946-40e5-3413-bc7b-085f8d220799","url":"https://s3.amazonaws.com/deeappservice.prod.notificationtones/system_alerts_melodic_01.mp3"},{"assetId":"e829df39-da2a-3527-8e8f-3661ffd28f6b","url":"https://s3.amazonaws.com/deeappservice.prod.notificationtones/alarm_short_alert"}],"assetPlayOrder":["3af1a946-40e5-3413-bc7b-085f8d220799"],"loopPauseInMilliSeconds":4000,"token":"amzn1.as-ct.v1.Domain:Application:NotificationsV4#DNID#35fccef0-5a71-3942-9151-baf432906528","backgroundAlertAsset":"e829df39-da2a-3527-8e8f-3661ffd28f6b"}}
     */

    private DirectiveBean directive;

    public DirectiveBean getDirective() {
        return directive;
    }

    public void setDirective(DirectiveBean directive) {
        this.directive = directive;
    }

    public static class DirectiveBean {
        /**
         * header : {"namespace":"Alerts","name":"SetAlert","messageId":"af70ad5c-b01d-4015-bd44-668b73124eff"}
         * payload : {"type":"ALARM","scheduledTime":"2018-04-03T16:00:00+0000","assets":[{"assetId":"3af1a946-40e5-3413-bc7b-085f8d220799","url":"https://s3.amazonaws.com/deeappservice.prod.notificationtones/system_alerts_melodic_01.mp3"},{"assetId":"e829df39-da2a-3527-8e8f-3661ffd28f6b","url":"https://s3.amazonaws.com/deeappservice.prod.notificationtones/alarm_short_alert"}],"assetPlayOrder":["3af1a946-40e5-3413-bc7b-085f8d220799"],"loopPauseInMilliSeconds":4000,"token":"amzn1.as-ct.v1.Domain:Application:NotificationsV4#DNID#35fccef0-5a71-3942-9151-baf432906528","backgroundAlertAsset":"e829df39-da2a-3527-8e8f-3661ffd28f6b"}
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

        public static class HeaderBean {
            /**
             * namespace : Alerts
             * name : SetAlert
             * messageId : af70ad5c-b01d-4015-bd44-668b73124eff
             */

            private String namespace;
            private String name;
            private String messageId;

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
        }

        public static class PayloadBean {
            /**
             * type : ALARM
             * scheduledTime : 2018-04-03T16:00:00+0000
             * assets : [{"assetId":"3af1a946-40e5-3413-bc7b-085f8d220799","url":"https://s3.amazonaws.com/deeappservice.prod.notificationtones/system_alerts_melodic_01.mp3"},{"assetId":"e829df39-da2a-3527-8e8f-3661ffd28f6b","url":"https://s3.amazonaws.com/deeappservice.prod.notificationtones/alarm_short_alert"}]
             * assetPlayOrder : ["3af1a946-40e5-3413-bc7b-085f8d220799"]
             * loopPauseInMilliSeconds : 4000
             * token : amzn1.as-ct.v1.Domain:Application:NotificationsV4#DNID#35fccef0-5a71-3942-9151-baf432906528
             * backgroundAlertAsset : e829df39-da2a-3527-8e8f-3661ffd28f6b
             */

            private String type;
            private String scheduledTime;
            private long loopPauseInMilliSeconds;
            private long loopCount;
            private String token;
            private String backgroundAlertAsset;
            private List<AssetsBean> assets;
            private List<String> assetPlayOrder;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getScheduledTime() {
                return scheduledTime;
            }

            public void setScheduledTime(String scheduledTime) {
                this.scheduledTime = scheduledTime;
            }

            public long getLoopPauseInMilliSeconds() {
                return loopPauseInMilliSeconds;
            }

            public void setLoopPauseInMilliSeconds(int loopPauseInMilliSeconds) {
                this.loopPauseInMilliSeconds = loopPauseInMilliSeconds;
            }

            public void setLoopCount(long loopCount) {
                this.loopCount = loopCount;
            }

            public long getLoopCount() {
                return loopCount;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getBackgroundAlertAsset() {
                return backgroundAlertAsset;
            }

            public void setBackgroundAlertAsset(String backgroundAlertAsset) {
                this.backgroundAlertAsset = backgroundAlertAsset;
            }

            public List<AssetsBean> getAssets() {
                return assets;
            }

            public void setAssets(List<AssetsBean> assets) {
                this.assets = assets;
            }

            public List<String> getAssetPlayOrder() {
                return assetPlayOrder;
            }

            public void setAssetPlayOrder(List<String> assetPlayOrder) {
                this.assetPlayOrder = assetPlayOrder;
            }

            public static class AssetsBean {
                /**
                 * assetId : 3af1a946-40e5-3413-bc7b-085f8d220799
                 * url : https://s3.amazonaws.com/deeappservice.prod.notificationtones/system_alerts_melodic_01.mp3
                 */

                private String assetId;
                private String url;

                public String getAssetId() {
                    return assetId;
                }

                public void setAssetId(String assetId) {
                    this.assetId = assetId;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }
}
