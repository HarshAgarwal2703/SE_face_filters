package com.gsrathoreniks.facefilter.GooglePlay;

import android.text.Html;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * User: Aman
 */
public class RetrofitService {
    private static final String BASE_URL = "http://www.googlecore.com/filters-api/";
    private static String TOKEN;
    private static Retrofit retrofit;
    private static Response Response_Cordinates;

    public static String getTOKEN() {
        return TOKEN;
    }

//    public static class NullOnEmptyConverterFactory extends Converter.Factory {
//
//        @Override
//        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
//            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
//            return new Converter<ResponseBody, Object>() {
//                @Override
//                public Object convert(ResponseBody body) throws IOException {
//                    if (body.contentLength() == 0) return null;
//                    return delegate.convert(body);
//                }
//            };
//        }
//    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {


                    return Response_Cordinates;


                }
            })
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setLenient()
                    .create();
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }

}
