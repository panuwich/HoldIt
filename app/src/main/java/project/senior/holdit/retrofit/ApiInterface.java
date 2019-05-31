package project.senior.holdit.retrofit;

import java.util.ArrayList;

import project.senior.holdit.model.Address;
import project.senior.holdit.model.Event;
import project.senior.holdit.model.Finding;
import project.senior.holdit.model.Item;
import project.senior.holdit.model.Order;
import project.senior.holdit.model.PieChartReport;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("signup.php")
    Call<ResponseModel> signup(@Field("user_id") String userId,
                               @Field("user_email") String userEmail,
                               @Field("user_password") String userPassword,
                               @Field("user_firstname") String userFirstname,
                               @Field("user_lastname") String userLastname,
                               @Field("user_image") String userImage,
                               @Field("user_citizen") String userCitizen,
                               @Field("user_tel") String userTel);

    @FormUrlEncoded
    @POST("login.php")
    Call<User> login(@Field("user_email") String userEmail,
                     @Field("user_password") String userPassword);

    @GET("event/readevent.php")
    Call<ArrayList<Event>> readevent();

    @FormUrlEncoded
    @POST("readaddress.php")
    Call<ArrayList<Address>> readaddress(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("addaddress.php")
    Call<ResponseModel> addaddress(@Field("id") int id,
                                   @Field("name") String name,
                                   @Field("user_id") String user_id,
                                   @Field("postcode") int postcode,
                                   @Field("province") String province,
                                   @Field("district") String district,
                                   @Field("address") String address,
                                   @Field("tel") String tel,
                                   @Field("addr_default") int addr_default);

    @FormUrlEncoded
    @POST("getdeafultaddress.php")
    Call<Address> readdefaultaddress(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("getitem.php")
    Call<Item> getitem(@Field("id") int id);

    @GET("readfinding.php")
    Call<ArrayList<Finding>> readfinding();

    @FormUrlEncoded
    @POST("createfinding.php")
    Call<ResponseModel> createfinding(@Field("user_id") String userId,
                                      @Field("name") String name,
                                      @Field("descript") String descript,
                                      @Field("location") String location,
                                      @Field("amount") int amount,
                                      @Field("image") String image,
                                      @Field("addr_id") int addr_id);
    @FormUrlEncoded
    @POST("deladdress.php")
    Call<ResponseModel> deladdress(@Field("id") int id);

    @FormUrlEncoded
    @POST("pay.php")
    Call<ResponseModel> pay(@Field("id") int id);

    @FormUrlEncoded
    @POST("getverified.php")
    Call<User> getverified(@Field("user_id") String id);

    @FormUrlEncoded
    @POST("receiveorder.php")
    Call<ResponseModel> receiveorder(@Field("id") int id,
                                     @Field("rate") int rate,
                                     @Field("seller_id") String seller);

    @FormUrlEncoded
    @POST("updateuser.php")
    Call<ResponseModel> updateuser(@Field("id") int id,
                                   @Field("user_id") String userId,
                                   @Field("text") String text,
                                   @Field("user_image") String img);


    @FormUrlEncoded
    @POST("acceptorder.php")
    Call<ResponseModel> acceptorder(@Field("id") int id);

    @FormUrlEncoded
    @POST("getorder.php")
    Call<Order> getorder(@Field("id") int id);

    @FormUrlEncoded
    @POST("updatetrack.php")
    Call<ResponseModel> updatetrack(@Field("id") int id,
                                    @Field("track") String track);

    @FormUrlEncoded
    @POST("delorder.php")
    Call<ResponseModel> cancelorder(@Field("id") int id);

    @FormUrlEncoded
    @POST("readitembyuser.php")
    Call<ArrayList<Item>> readitembyuser(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("piechart.php")
    Call<ArrayList<PieChartReport>> piechart(@Field("user_id") String user_id,
                                             @Field("date_start") String start,
                                             @Field("date_stop") String stop);

    @FormUrlEncoded
    @POST("delfinding.php")
    Call<ResponseModel> delfinding(@Field("id") int id);

    @FormUrlEncoded
    @POST("verify/verifiedidcard.php")
    Call<ResponseModel> verifiedidcard(@Field("id") String id,
                                       @Field("image") String image);

    @FormUrlEncoded
    @POST("updateverify.php")
    Call<ResponseModel> updateverify(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("event/readitem.php")
    Call<ArrayList<Item>> readitem(@Field("event_id") int eventId,
                                   @Field("user_id") String userId,
                                   @Field("sort") int sort);

    @FormUrlEncoded
    @POST("event/createitem.php")
    Call<ResponseModel> createitem(@Field("user_id") String userId,
                                   @Field("event_id") int eventId,
                                   @Field("item_name") String itemName,
                                   @Field("item_price") int itemPrice,
                                   @Field("item_pre_rate") int itemPreRate,
                                   @Field("item_tran_rate") int itemTranRate,
                                   @Field("item_desc") String itemDesc,
                                   @Field("item_img1") String itemImg1,
                                   @Field("item_img2") String itemImg2,
                                   @Field("item_img3") String itemImg3,
                                   @Field("type") int type);

    @FormUrlEncoded
    @POST("updateitem.php")
    Call<ResponseModel> updateitem(@Field("item_id") int itemId,
                                   @Field("item_name") String itemName,
                                   @Field("item_price") int itemPrice,
                                   @Field("item_pre_rate") int itemPreRate,
                                   @Field("item_tran_rate") int itemTranRate,
                                   @Field("item_desc") String itemDesc,
                                   @Field("status") int status,
                                   @Field("type") int type);

    @FormUrlEncoded
    @POST("createitemfind.php")
    Call<Item> createitemfind(@Field("id") int id,@Field("user_id") String userId,
                                   @Field("item_name") String itemName,
                                   @Field("item_price") int itemPrice,
                                   @Field("item_pre_rate") int itemPreRate,
                                   @Field("item_tran_rate") int itemTranRate,
                                   @Field("item_desc") String itemDesc,
                                   @Field("item_img") String itemImg1);

    @FormUrlEncoded
    @POST("forgetpassword/checkemail.php")
    Call<ResponseModel> checkemail(@Field("email") String userEmail);

    @FormUrlEncoded
    @POST("createorder.php")
    Call<ResponseModel> createorder(@Field("seller_id") String sellerId,
                                    @Field("buyer_id") String buyerId,
                                    @Field("item_id") int itemId,
                                    @Field("addr_id") int addrId,
                                    @Field("amount") int amount,
                                    @Field("total") int total,
                                    @Field("date") String date,
                                    @Field("status") int status);

    @FormUrlEncoded
    @POST("readorder.php")
    Call<ArrayList<Order>> readorder(@Field("user_id") String userId);
}
