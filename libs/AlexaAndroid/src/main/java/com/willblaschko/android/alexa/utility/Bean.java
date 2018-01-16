package com.willblaschko.android.alexa.utility;

/**
 * Created by user001 on 2018-1-12.
 */

public class Bean {

    /**
     * directive : {"header":{"namespace":"TemplateRuntime","name":"RenderTemplate","messageId":"9e15699d-619a-4059-8004-21e3a95facc4","dialogRequestId":"dialogRequest-321"},"payload":{"type":"BodyTemplate1","token":"339df78a-633c-4f53-ab2d-d96e48dc114a","title":{"subTitle":"newFun","mainTitle":"SessionSpeechlet - WhatsMyColorIntent"},"textField":"SessionSpeechlet - I'm not sure what your favorite color is. You can say, my favorite color is red."}}
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
         * header : {"namespace":"TemplateRuntime","name":"RenderTemplate","messageId":"9e15699d-619a-4059-8004-21e3a95facc4","dialogRequestId":"dialogRequest-321"}
         * payload : {"type":"BodyTemplate1","token":"339df78a-633c-4f53-ab2d-d96e48dc114a","title":{"subTitle":"newFun","mainTitle":"SessionSpeechlet - WhatsMyColorIntent"},"textField":"SessionSpeechlet - I'm not sure what your favorite color is. You can say, my favorite color is red."}
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
             * namespace : TemplateRuntime
             * name : RenderTemplate
             * messageId : 9e15699d-619a-4059-8004-21e3a95facc4
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
        }

        public static class PayloadBean {
            /**
             * type : BodyTemplate1
             * token : 339df78a-633c-4f53-ab2d-d96e48dc114a
             * title : {"subTitle":"newFun","mainTitle":"SessionSpeechlet - WhatsMyColorIntent"}
             * textField : SessionSpeechlet - I'm not sure what your favorite color is. You can say, my favorite color is red.
             */

            private String type;
            private String token;
            private TitleBean title;
            private String textField;

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

            public TitleBean getTitle() {
                return title;
            }

            public void setTitle(TitleBean title) {
                this.title = title;
            }

            public String getTextField() {
                return textField;
            }

            public void setTextField(String textField) {
                this.textField = textField;
            }

            public static class TitleBean {
                /**
                 * subTitle : newFun
                 * mainTitle : SessionSpeechlet - WhatsMyColorIntent
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
            }
        }
    }
}
