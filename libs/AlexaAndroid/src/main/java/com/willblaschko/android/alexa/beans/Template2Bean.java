package com.willblaschko.android.alexa.beans;

import java.util.List;

/**
 * Created by user001 on 2018-3-8.
 */

public class Template2Bean {

    /**
     * directive : {"header":{"namespace":"TemplateRuntime","name":"RenderTemplate","messageId":"18c2f542-f5b1-4a8a-816c-e9a2e27a73be","dialogRequestId":"dialogRequest-321"},"payload":{"type":"BodyTemplate2","token":"4f2841c6-c0c6-44c5-b19c-4ab08cfbe7a8","title":{"mainTitle":"Tell me who is James Harden"},"textField":"James Edward Harden Jr. is an American professional basketball player for the Houston Rockets of the National Basketball Association.","image":{"sources":[{"size":"SMALL","url":"https://m.media-amazon.com/images/S/com.evi.images-irs/62/62f0321335be09c5965e43db86fed610.jpg"},{"size":"MEDIUM","url":"https://m.media-amazon.com/images/S/com.evi.images-irs/62/62f0321335be09c5965e43db86fed610.jpg"},{"size":"LARGE","url":"https://m.media-amazon.com/images/S/com.evi.images-irs/62/62f0321335be09c5965e43db86fed610.jpg"}]}}}
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
         * header : {"namespace":"TemplateRuntime","name":"RenderTemplate","messageId":"18c2f542-f5b1-4a8a-816c-e9a2e27a73be","dialogRequestId":"dialogRequest-321"}
         * payload : {"type":"BodyTemplate2","token":"4f2841c6-c0c6-44c5-b19c-4ab08cfbe7a8","title":{"mainTitle":"Tell me who is James Harden"},"textField":"James Edward Harden Jr. is an American professional basketball player for the Houston Rockets of the National Basketball Association.","image":{"sources":[{"size":"SMALL","url":"https://m.media-amazon.com/images/S/com.evi.images-irs/62/62f0321335be09c5965e43db86fed610.jpg"},{"size":"MEDIUM","url":"https://m.media-amazon.com/images/S/com.evi.images-irs/62/62f0321335be09c5965e43db86fed610.jpg"},{"size":"LARGE","url":"https://m.media-amazon.com/images/S/com.evi.images-irs/62/62f0321335be09c5965e43db86fed610.jpg"}]}}
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
             * messageId : 18c2f542-f5b1-4a8a-816c-e9a2e27a73be
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
             * type : BodyTemplate2
             * token : 4f2841c6-c0c6-44c5-b19c-4ab08cfbe7a8
             * title : {"mainTitle":"Tell me who is James Harden"}
             * textField : James Edward Harden Jr. is an American professional basketball player for the Houston Rockets of the National Basketball Association.
             * image : {"sources":[{"size":"SMALL","url":"https://m.media-amazon.com/images/S/com.evi.images-irs/62/62f0321335be09c5965e43db86fed610.jpg"},{"size":"MEDIUM","url":"https://m.media-amazon.com/images/S/com.evi.images-irs/62/62f0321335be09c5965e43db86fed610.jpg"},{"size":"LARGE","url":"https://m.media-amazon.com/images/S/com.evi.images-irs/62/62f0321335be09c5965e43db86fed610.jpg"}]}
             */

            private String type;
            private String token;
            private TitleBean title;
            private String textField;
            private ImageBean image;

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

            public ImageBean getImage() {
                return image;
            }

            public void setImage(ImageBean image) {
                this.image = image;
            }

            public static class TitleBean {
                /**
                 * mainTitle : Tell me who is James Harden
                 */

                private String mainTitle;

                public String getMainTitle() {
                    return mainTitle;
                }

                public void setMainTitle(String mainTitle) {
                    this.mainTitle = mainTitle;
                }
            }

            public static class ImageBean {
                private List<SourcesBean> sources;

                public List<SourcesBean> getSourcesBean() {
                    return sources;
                }

                public void setSourcesBean(SourcesBean sourcesBean) {
                    this.sourcesBean = sourcesBean;
                }

                private SourcesBean sourcesBean;

                public List<SourcesBean> getSources() {
                    return sources;
                }

                public void setSources(List<SourcesBean> sources) {
                    this.sources = sources;
                }

                public static class SourcesBean {
                    /**
                     * size : SMALL
                     * url : https://m.media-amazon.com/images/S/com.evi.images-irs/62/62f0321335be09c5965e43db86fed610.jpg
                     */

                    private String size;
                    private String url;

                    public String getSize() {
                        return size;
                    }

                    public void setSize(String size) {
                        this.size = size;
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
}
