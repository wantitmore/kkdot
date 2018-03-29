package com.willblaschko.android.alexa.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfoBean implements Parcelable {


    /**
     * directive : {"header":{"namespace":"TemplateRuntime","name":"RenderPlayerInfo","messageId":"7db3fb6c-67b6-4e0d-9cbe-1da21368a29d"},"payload":{"content":{"provider":{"name":"NPR","logo":{"sources":[{"url":"https://s3.amazonaws.com/CAPS-SSE/echo_developer/1841cf426f264220b4768e62231ad8bc/APP_ICON_LARGE?versionId=IiYEZUOv3v8LE65s0hhGOt.T3fSpWKyJ&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524344495&Signature=kuvOvGi3geMNTXH8y%2BSQZxdiFeQ%3D"}]}},"art":{"sources":[{"url":"https://s3.amazonaws.com/CAPS-SSE/echo_developer/7e71c6f1.d48b98d3.1fe80d6058354d16a877a44bcb4e499f/APP_ICON_LARGE?versionId=2DwmAuIvyO_.B7mBC36Gy1IgjC6Kjljj&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524288436&Signature=fx00Fatek%2F4NH1s2kap4c18q%2FuA%3D"}]},"mediaLengthInMilliseconds":0,"title":"NPR Hourly News Summary"},"audioItemId":"DailyBriefingPrompt.2770d992-74bf-47eb-86f4-66bf8ea2b126:ChannelIntroduction:0","controls":[{"selected":false,"name":"PLAY_PAUSE","enabled":false,"type":"BUTTON"},{"selected":false,"name":"NEXT","enabled":false,"type":"BUTTON"},{"selected":false,"name":"PREVIOUS","enabled":false,"type":"BUTTON"},{"selected":false,"name":"REPEAT","enabled":false,"type":"BUTTON"},{"selected":false,"name":"SHUFFLE","enabled":false,"type":"TOGGLE"}]}}
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
         * header : {"namespace":"TemplateRuntime","name":"RenderPlayerInfo","messageId":"7db3fb6c-67b6-4e0d-9cbe-1da21368a29d"}
         * payload : {"content":{"provider":{"name":"NPR","logo":{"sources":[{"url":"https://s3.amazonaws.com/CAPS-SSE/echo_developer/1841cf426f264220b4768e62231ad8bc/APP_ICON_LARGE?versionId=IiYEZUOv3v8LE65s0hhGOt.T3fSpWKyJ&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524344495&Signature=kuvOvGi3geMNTXH8y%2BSQZxdiFeQ%3D"}]}},"art":{"sources":[{"url":"https://s3.amazonaws.com/CAPS-SSE/echo_developer/7e71c6f1.d48b98d3.1fe80d6058354d16a877a44bcb4e499f/APP_ICON_LARGE?versionId=2DwmAuIvyO_.B7mBC36Gy1IgjC6Kjljj&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524288436&Signature=fx00Fatek%2F4NH1s2kap4c18q%2FuA%3D"}]},"mediaLengthInMilliseconds":0,"title":"NPR Hourly News Summary"},"audioItemId":"DailyBriefingPrompt.2770d992-74bf-47eb-86f4-66bf8ea2b126:ChannelIntroduction:0","controls":[{"selected":false,"name":"PLAY_PAUSE","enabled":false,"type":"BUTTON"},{"selected":false,"name":"NEXT","enabled":false,"type":"BUTTON"},{"selected":false,"name":"PREVIOUS","enabled":false,"type":"BUTTON"},{"selected":false,"name":"REPEAT","enabled":false,"type":"BUTTON"},{"selected":false,"name":"SHUFFLE","enabled":false,"type":"TOGGLE"}]}
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
             * name : RenderPlayerInfo
             * messageId : 7db3fb6c-67b6-4e0d-9cbe-1da21368a29d
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.namespace);
                dest.writeString(this.name);
                dest.writeString(this.messageId);
            }

            public HeaderBean() {
            }

            protected HeaderBean(Parcel in) {
                this.namespace = in.readString();
                this.name = in.readString();
                this.messageId = in.readString();
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
             * content : {"provider":{"name":"NPR","logo":{"sources":[{"url":"https://s3.amazonaws.com/CAPS-SSE/echo_developer/1841cf426f264220b4768e62231ad8bc/APP_ICON_LARGE?versionId=IiYEZUOv3v8LE65s0hhGOt.T3fSpWKyJ&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524344495&Signature=kuvOvGi3geMNTXH8y%2BSQZxdiFeQ%3D"}]}},"art":{"sources":[{"url":"https://s3.amazonaws.com/CAPS-SSE/echo_developer/7e71c6f1.d48b98d3.1fe80d6058354d16a877a44bcb4e499f/APP_ICON_LARGE?versionId=2DwmAuIvyO_.B7mBC36Gy1IgjC6Kjljj&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524288436&Signature=fx00Fatek%2F4NH1s2kap4c18q%2FuA%3D"}]},"mediaLengthInMilliseconds":0,"title":"NPR Hourly News Summary"}
             * audioItemId : DailyBriefingPrompt.2770d992-74bf-47eb-86f4-66bf8ea2b126:ChannelIntroduction:0
             * controls : [{"selected":false,"name":"PLAY_PAUSE","enabled":false,"type":"BUTTON"},{"selected":false,"name":"NEXT","enabled":false,"type":"BUTTON"},{"selected":false,"name":"PREVIOUS","enabled":false,"type":"BUTTON"},{"selected":false,"name":"REPEAT","enabled":false,"type":"BUTTON"},{"selected":false,"name":"SHUFFLE","enabled":false,"type":"TOGGLE"}]
             */

            private ContentBean content;
            private String audioItemId;
            private List<ControlsBean> controls;

            public ContentBean getContent() {
                return content;
            }

            public void setContent(ContentBean content) {
                this.content = content;
            }

            public String getAudioItemId() {
                return audioItemId;
            }

            public void setAudioItemId(String audioItemId) {
                this.audioItemId = audioItemId;
            }

            public List<ControlsBean> getControls() {
                return controls;
            }

            public void setControls(List<ControlsBean> controls) {
                this.controls = controls;
            }

            public static class ContentBean implements Parcelable {
                /**
                 * provider : {"name":"NPR","logo":{"sources":[{"url":"https://s3.amazonaws.com/CAPS-SSE/echo_developer/1841cf426f264220b4768e62231ad8bc/APP_ICON_LARGE?versionId=IiYEZUOv3v8LE65s0hhGOt.T3fSpWKyJ&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524344495&Signature=kuvOvGi3geMNTXH8y%2BSQZxdiFeQ%3D"}]}}
                 * art : {"sources":[{"url":"https://s3.amazonaws.com/CAPS-SSE/echo_developer/7e71c6f1.d48b98d3.1fe80d6058354d16a877a44bcb4e499f/APP_ICON_LARGE?versionId=2DwmAuIvyO_.B7mBC36Gy1IgjC6Kjljj&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524288436&Signature=fx00Fatek%2F4NH1s2kap4c18q%2FuA%3D"}]}
                 * mediaLengthInMilliseconds : 0
                 * title : NPR Hourly News Summary
                 */

                private ProviderBean provider;
                private ArtBean art;
                private int mediaLengthInMilliseconds;
                private String title;

                public String getHeader() {
                    return header;
                }

                public void setHeader(String header) {
                    this.header = header;
                }

                private String header;

                public ProviderBean getProvider() {
                    return provider;
                }

                public void setProvider(ProviderBean provider) {
                    this.provider = provider;
                }

                public ArtBean getArt() {
                    return art;
                }

                public void setArt(ArtBean art) {
                    this.art = art;
                }

                public int getMediaLengthInMilliseconds() {
                    return mediaLengthInMilliseconds;
                }

                public void setMediaLengthInMilliseconds(int mediaLengthInMilliseconds) {
                    this.mediaLengthInMilliseconds = mediaLengthInMilliseconds;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public static class ProviderBean implements Parcelable {
                    /**
                     * name : NPR
                     * logo : {"sources":[{"url":"https://s3.amazonaws.com/CAPS-SSE/echo_developer/1841cf426f264220b4768e62231ad8bc/APP_ICON_LARGE?versionId=IiYEZUOv3v8LE65s0hhGOt.T3fSpWKyJ&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524344495&Signature=kuvOvGi3geMNTXH8y%2BSQZxdiFeQ%3D"}]}
                     */

                    private String name;
                    private LogoBean logo;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public LogoBean getLogo() {
                        return logo;
                    }

                    public void setLogo(LogoBean logo) {
                        this.logo = logo;
                    }

                    public static class LogoBean implements Parcelable {
                        private List<SourcesBean> sources;

                        public List<SourcesBean> getSources() {
                            return sources;
                        }

                        public void setSources(List<SourcesBean> sources) {
                            this.sources = sources;
                        }

                        public static class SourcesBean implements Parcelable {
                            /**
                             * url : https://s3.amazonaws.com/CAPS-SSE/echo_developer/1841cf426f264220b4768e62231ad8bc/APP_ICON_LARGE?versionId=IiYEZUOv3v8LE65s0hhGOt.T3fSpWKyJ&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524344495&Signature=kuvOvGi3geMNTXH8y%2BSQZxdiFeQ%3D
                             */

                            private String url;

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
                                dest.writeString(this.url);
                            }

                            public SourcesBean() {
                            }

                            protected SourcesBean(Parcel in) {
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
                            dest.writeList(this.sources);
                        }

                        public LogoBean() {
                        }

                        protected LogoBean(Parcel in) {
                            this.sources = new ArrayList<SourcesBean>();
                            in.readList(this.sources, SourcesBean.class.getClassLoader());
                        }

                        public static final Creator<LogoBean> CREATOR = new Creator<LogoBean>() {
                            @Override
                            public LogoBean createFromParcel(Parcel source) {
                                return new LogoBean(source);
                            }

                            @Override
                            public LogoBean[] newArray(int size) {
                                return new LogoBean[size];
                            }
                        };
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(this.name);
                        dest.writeParcelable(this.logo, flags);
                    }

                    public ProviderBean() {
                    }

                    protected ProviderBean(Parcel in) {
                        this.name = in.readString();
                        this.logo = in.readParcelable(LogoBean.class.getClassLoader());
                    }

                    public static final Creator<ProviderBean> CREATOR = new Creator<ProviderBean>() {
                        @Override
                        public ProviderBean createFromParcel(Parcel source) {
                            return new ProviderBean(source);
                        }

                        @Override
                        public ProviderBean[] newArray(int size) {
                            return new ProviderBean[size];
                        }
                    };
                }

                public static class ArtBean implements Parcelable {
                    private List<SourcesBeanX> sources;

                    public List<SourcesBeanX> getSources() {
                        return sources;
                    }

                    public void setSources(List<SourcesBeanX> sources) {
                        this.sources = sources;
                    }

                    public static class SourcesBeanX implements Parcelable {
                        /**
                         * url : https://s3.amazonaws.com/CAPS-SSE/echo_developer/7e71c6f1.d48b98d3.1fe80d6058354d16a877a44bcb4e499f/APP_ICON_LARGE?versionId=2DwmAuIvyO_.B7mBC36Gy1IgjC6Kjljj&AWSAccessKeyId=AKIAI3X6PUXD7CU5F5BQ&Expires=1524288436&Signature=fx00Fatek%2F4NH1s2kap4c18q%2FuA%3D
                         */

                        private String url;

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
                            dest.writeString(this.url);
                        }

                        public SourcesBeanX() {
                        }

                        protected SourcesBeanX(Parcel in) {
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
                        dest.writeList(this.sources);
                    }

                    public ArtBean() {
                    }

                    protected ArtBean(Parcel in) {
                        this.sources = new ArrayList<SourcesBeanX>();
                        in.readList(this.sources, SourcesBeanX.class.getClassLoader());
                    }

                    public static final Creator<ArtBean> CREATOR = new Creator<ArtBean>() {
                        @Override
                        public ArtBean createFromParcel(Parcel source) {
                            return new ArtBean(source);
                        }

                        @Override
                        public ArtBean[] newArray(int size) {
                            return new ArtBean[size];
                        }
                    };
                }

                public ContentBean() {
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeParcelable(this.provider, flags);
                    dest.writeParcelable(this.art, flags);
                    dest.writeInt(this.mediaLengthInMilliseconds);
                    dest.writeString(this.title);
                    dest.writeString(this.header);
                }

                protected ContentBean(Parcel in) {
                    this.provider = in.readParcelable(ProviderBean.class.getClassLoader());
                    this.art = in.readParcelable(ArtBean.class.getClassLoader());
                    this.mediaLengthInMilliseconds = in.readInt();
                    this.title = in.readString();
                    this.header = in.readString();
                }

                public static final Creator<ContentBean> CREATOR = new Creator<ContentBean>() {
                    @Override
                    public ContentBean createFromParcel(Parcel source) {
                        return new ContentBean(source);
                    }

                    @Override
                    public ContentBean[] newArray(int size) {
                        return new ContentBean[size];
                    }
                };
            }

            public static class ControlsBean implements Parcelable {
                /**
                 * selected : false
                 * name : PLAY_PAUSE
                 * enabled : false
                 * type : BUTTON
                 */

                private boolean selected;
                private String name;
                private boolean enabled;
                private String type;

                public boolean isSelected() {
                    return selected;
                }

                public void setSelected(boolean selected) {
                    this.selected = selected;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public boolean isEnabled() {
                    return enabled;
                }

                public void setEnabled(boolean enabled) {
                    this.enabled = enabled;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
                    dest.writeString(this.name);
                    dest.writeByte(this.enabled ? (byte) 1 : (byte) 0);
                    dest.writeString(this.type);
                }

                public ControlsBean() {
                }

                protected ControlsBean(Parcel in) {
                    this.selected = in.readByte() != 0;
                    this.name = in.readString();
                    this.enabled = in.readByte() != 0;
                    this.type = in.readString();
                }

                public static final Creator<ControlsBean> CREATOR = new Creator<ControlsBean>() {
                    @Override
                    public ControlsBean createFromParcel(Parcel source) {
                        return new ControlsBean(source);
                    }

                    @Override
                    public ControlsBean[] newArray(int size) {
                        return new ControlsBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeParcelable(this.content, flags);
                dest.writeString(this.audioItemId);
                dest.writeList(this.controls);
            }

            public PayloadBean() {
            }

            protected PayloadBean(Parcel in) {
                this.content = in.readParcelable(ContentBean.class.getClassLoader());
                this.audioItemId = in.readString();
                this.controls = new ArrayList<ControlsBean>();
                in.readList(this.controls, ControlsBean.class.getClassLoader());
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

    public PlayerInfoBean() {
    }

    protected PlayerInfoBean(Parcel in) {
        this.directive = in.readParcelable(DirectiveBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<PlayerInfoBean> CREATOR = new Parcelable.Creator<PlayerInfoBean>() {
        @Override
        public PlayerInfoBean createFromParcel(Parcel source) {
            return new PlayerInfoBean(source);
        }

        @Override
        public PlayerInfoBean[] newArray(int size) {
            return new PlayerInfoBean[size];
        }
    };
}
