package com.willblaschko.android.alexa.beans;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ListTemplate1Bean implements Parcelable {

    /**
     * directive : {"header":{"namespace":"TemplateRuntime","name":"RenderTemplate","messageId":"ef347560-acfb-495a-8130-e8952c167906","dialogRequestId":"dialogRequest-321"},"payload":{"type":"ListTemplate1","token":"1ba557b9-69c6-4179-99ee-e8dfc702ea40","listItems":[{"leftTextField":"1.","rightTextField":"eggs"},{"leftTextField":"2.","rightTextField":"horn"},{"leftTextField":"3.","rightTextField":"google dlna"}],"title":{"mainTitle":"Shopping List"}}}
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
         * header : {"namespace":"TemplateRuntime","name":"RenderTemplate","messageId":"ef347560-acfb-495a-8130-e8952c167906","dialogRequestId":"dialogRequest-321"}
         * payload : {"type":"ListTemplate1","token":"1ba557b9-69c6-4179-99ee-e8dfc702ea40","listItems":[{"leftTextField":"1.","rightTextField":"eggs"},{"leftTextField":"2.","rightTextField":"horn"},{"leftTextField":"3.","rightTextField":"google dlna"}],"title":{"mainTitle":"Shopping List"}}
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
             * messageId : ef347560-acfb-495a-8130-e8952c167906
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
             * type : ListTemplate1
             * token : 1ba557b9-69c6-4179-99ee-e8dfc702ea40
             * listItems : [{"leftTextField":"1.","rightTextField":"eggs"},{"leftTextField":"2.","rightTextField":"horn"},{"leftTextField":"3.","rightTextField":"google dlna"}]
             * title : {"mainTitle":"Shopping List"}
             */

            private String type;
            private String token;
            private TitleBean title;
            private List<ListItemsBean> listItems;

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

            public List<ListItemsBean> getListItems() {
                return listItems;
            }

            public void setListItems(List<ListItemsBean> listItems) {
                this.listItems = listItems;
            }

            public static class TitleBean implements Parcelable {
                /**
                 * mainTitle : Shopping List
                 */

                private String mainTitle;
                private String subTitle;

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
                    dest.writeString(this.mainTitle);
                    dest.writeString(this.subTitle);
                }

                public TitleBean() {
                }

                protected TitleBean(Parcel in) {
                    this.mainTitle = in.readString();
                    this.subTitle = in.readString();
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

            public static class ListItemsBean implements Parcelable {
                /**
                 * leftTextField : 1.
                 * rightTextField : eggs
                 */

                private String leftTextField;
                private String rightTextField;

                public String getLeftTextField() {
                    return leftTextField;
                }

                public void setLeftTextField(String leftTextField) {
                    this.leftTextField = leftTextField;
                }

                public String getRightTextField() {
                    return rightTextField;
                }

                public void setRightTextField(String rightTextField) {
                    this.rightTextField = rightTextField;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.leftTextField);
                    dest.writeString(this.rightTextField);
                }

                public ListItemsBean() {
                }

                protected ListItemsBean(Parcel in) {
                    this.leftTextField = in.readString();
                    this.rightTextField = in.readString();
                }

                public static final Creator<ListItemsBean> CREATOR = new Creator<ListItemsBean>() {
                    @Override
                    public ListItemsBean createFromParcel(Parcel source) {
                        return new ListItemsBean(source);
                    }

                    @Override
                    public ListItemsBean[] newArray(int size) {
                        return new ListItemsBean[size];
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
                dest.writeList(this.listItems);
            }

            public PayloadBean() {
            }

            protected PayloadBean(Parcel in) {
                this.type = in.readString();
                this.token = in.readString();
                this.title = in.readParcelable(TitleBean.class.getClassLoader());
                this.listItems = new ArrayList<ListItemsBean>();
                in.readList(this.listItems, ListItemsBean.class.getClassLoader());
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

    public ListTemplate1Bean() {
    }

    protected ListTemplate1Bean(Parcel in) {
        this.directive = in.readParcelable(DirectiveBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ListTemplate1Bean> CREATOR = new Parcelable.Creator<ListTemplate1Bean>() {
        @Override
        public ListTemplate1Bean createFromParcel(Parcel source) {
            return new ListTemplate1Bean(source);
        }

        @Override
        public ListTemplate1Bean[] newArray(int size) {
            return new ListTemplate1Bean[size];
        }
    };
}
