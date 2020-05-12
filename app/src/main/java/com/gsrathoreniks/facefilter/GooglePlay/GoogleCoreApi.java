package com.gsrathoreniks.facefilter.GooglePlay;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface GoogleCoreApi {

    @Multipart
    @POST(value = "userimagecords")
    Call<UserImageCords> userImage(@Part("user_details_id") String id,
                                           @Part MultipartBody.Part file);



    @Multipart
    @POST(value = "userimage")
    Call<UserFilters> userImage(@Part String filters);


}

