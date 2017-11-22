package com.star.eagleme.utils;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ByteConstants;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.io.File;

/**
 * Created by supertramp on 17/8/15.
 */
public class FrescoUtil
{

    public static void loadImage(SimpleDraweeView view, String path)
    {

        if (TextUtils.isEmpty(path))
        {
            return;
        }

        Uri uri = Uri.parse(path);
        if(uri == null)
        {
            return;
        }
        view.setImageURI(uri);

    }

    public static void loadImage(SimpleDraweeView view, File file)
    {

        Uri uri = Uri.fromFile(file);
        if(uri == null)
        {
            return;
        }
        view.setImageURI(uri);

    }

    public static void loadImage(SimpleDraweeView view, File file, int width, int height)
    {

        ResizeOptions options = new ResizeOptions(width, height);

        Uri uri = Uri.fromFile(file);
        if(uri == null)
        {
            return;
        }
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setResizeOptions(options)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(view.getController())
                .setImageRequest(request)
                .setCallerContext(uri)
                .build();

        view.setController(controller);

    }

    public static void loadImage(SimpleDraweeView view, String path, int width, int height)
    {

        ResizeOptions options = new ResizeOptions(width, height);

        Uri uri = Uri.parse(path);
        if(uri == null)
        {
            return;
        }

        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setResizeOptions(options)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(view.getController())
                .setImageRequest(request)
                .setCallerContext(uri)
                .build();

        view.setController(controller);

    }

    public static void loadGrayImage(SimpleDraweeView view, String path)
    {

        Postprocessor grayMeshPostprocessor = new BasePostprocessor()
        {
            @Override
            public void process(Bitmap bitmap)
            {
                 gray(bitmap);

            }
        };

        Uri uri = Uri.parse(path);
        if(uri == null)
        {
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(grayMeshPostprocessor)
                .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(view.getController())
                .build();

        view.setController(controller);

    }

    public static void getCacheBitmap(Context context, Uri uri, DataSubscriber mSubscriber)
    {

        if(uri == null)
        {
            return;
        }
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(mSubscriber, CallerThreadExecutor.getInstance());

    }

    public static void clearMemory(Context context)
    {

        try {

            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            if (imagePipeline != null)
            {
                imagePipeline.clearCaches();
            }

        }catch (Exception e){}

    }

    /**
     * fresco裁剪webp图片,减少内存消耗
     * @param targetView 目标view
     * @param file  图片url
     * @param controllerListener  监听
     * @return  DraweeController controller
     */
    public static DraweeController getDraweeController(DraweeView targetView, File file, ControllerListener controllerListener , @Nullable ResizeOptions resizeOptions)
    {

        Uri uri = Uri.fromFile(file);
        if(resizeOptions == null)
        {
            return Fresco.newDraweeControllerBuilder().setUri(uri).setControllerListener(controllerListener).build();
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(resizeOptions)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setOldController(targetView.getController())
                .setImageRequest(request)
                .setCallerContext(uri)
                .build();

        return controller;

    }

    /**
     * fresco: 配置DownsampleEnabled，对webP进行裁剪，减少内存
     * @param context 目标view
     * @return  ImagePipelineConfig
     */
    public static ImagePipelineConfig getImagePipelineConfig(Context context)
    {

        final MemoryCacheParams bmpCacheParams = new MemoryCacheParams(50 * ByteConstants.MB, 100, 5 * ByteConstants.MB, 5, ByteConstants.MB);//单个图片最大大小
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>()
        {

            @Override
            public MemoryCacheParams get()
            {
                return bmpCacheParams;
            }

        };

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setDownsampleEnabled(true)
                .setResizeAndRotateEnabledForNetwork(true)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                .build();
        return config;

    }

    public static void gray(Bitmap bitmap)
    {

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);

    }

    public static void TrimMemory(int level)
    {

        try {

            if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE)
            {
                ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
            }

        }catch (Exception e){}

    }

    public static void clearAllMemoryCaches()
    {

        try
        {

            ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();

        }catch (Exception e){}

    }

}
