package com.willblaschko.android.alexa.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user001 on 2018-1-12.
 */

public class Template1Bean implements Parcelable {

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

    public static class DirectiveBean implements Parcelable {
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

        public static class HeaderBean implements Parcelable {
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

            public static class TitleBean implements Parcelable {
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.type);
                dest.writeString(this.token);
                dest.writeParcelable(this.title, flags);
                dest.writeString(this.textField);
            }

            public PayloadBean() {
            }

            protected PayloadBean(Parcel in) {
                this.type = in.readString();
                this.token = in.readString();
                this.title = in.readParcelable(TitleBean.class.getClassLoader());
                this.textField = in.readString();
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

    public Template1Bean() {
    }

    protected Template1Bean(Parcel in) {
        this.directive = in.readParcelable(DirectiveBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Template1Bean> CREATOR = new Parcelable.Creator<Template1Bean>() {
        @Override
        public Template1Bean createFromParcel(Parcel source) {
            return new Template1Bean(source);
        }

        @Override
        public Template1Bean[] newArray(int size) {
            return new Template1Bean[size];
        }
    };
}
