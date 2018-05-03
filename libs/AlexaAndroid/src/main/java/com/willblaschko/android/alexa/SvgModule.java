package com.willblaschko.android.alexa;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;

import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.caverock.androidsvg.SVG;
import com.willblaschko.android.alexa.utility.svg.SvgDecoder;
import com.willblaschko.android.alexa.utility.svg.SvgDrawableTranscoder;

import java.io.InputStream;

/**
 * Created by user001 on 2018-4-28.
 */

@GlideModule
public final class SvgModule extends AppGlideModule {
    @Override
    public void registerComponents(Context context, Registry registry) {
        registry.register(SVG.class, PictureDrawable.class, new SvgDrawableTranscoder())
                .append(InputStream.class, SVG.class, new SvgDecoder());
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
