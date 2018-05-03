package com.willblaschko.android.alexa.utility.svg;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user001 on 2018-4-28.
 */

public class SvgDecoder implements ResourceDecoder<InputStream, SVG> {

    @Override
    public boolean handles(InputStream source, Options options) throws IOException {
        // TODO: Can we tell?
        return true;
    }

    public Resource<SVG> decode(InputStream source, int width, int height, Options options)
            throws IOException {
        try {
            SVG svg = SVG.getFromInputStream(source);

//            svg.setDocumentWidth(width);
//            svg.setDocumentHeight(height);
//            svg.setDocumentPreserveAspectRatio(STRETCH);
            return new SimpleResource<>(svg);
        } catch (SVGParseException ex) {
            throw new IOException("Cannot load SVG from stream", ex);
        }
    }
}
