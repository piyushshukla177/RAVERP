package com.rav.raverp.network;

import com.rav.raverp.data.model.api.ActivityLogModel;
import com.rav.raverp.data.model.api.AddAssociateModal;
import com.rav.raverp.data.model.api.AddCustomerEditCustomer;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.ApiUploadImageResponse;
import com.rav.raverp.data.model.api.AssociateWalletAmount;
import com.rav.raverp.data.model.api.BookingPlotListModal;
import com.rav.raverp.data.model.api.ChatModel;
import com.rav.raverp.data.model.api.ClaimTypeModel;
import com.rav.raverp.data.model.api.CommonModel;
import com.rav.raverp.data.model.api.CustomerAmount;
import com.rav.raverp.data.model.api.CustomerListModel;
import com.rav.raverp.data.model.api.CustomerPlotBookModel;
import com.rav.raverp.data.model.api.CustomerPlotBookingAccountDetailsModel;
import com.rav.raverp.data.model.api.CustomerPlotDetails;
import com.rav.raverp.data.model.api.CustomerPlotDetailsModel;
import com.rav.raverp.data.model.api.DashBoardModal;
import com.rav.raverp.data.model.api.DeleteCustomerModel;
import com.rav.raverp.data.model.api.DocumentTypeModel;
import com.rav.raverp.data.model.api.EditEmailModel;
import com.rav.raverp.data.model.api.EditMobileModel;
import com.rav.raverp.data.model.api.FollowUpListModel;
import com.rav.raverp.data.model.api.FollowUpModel;
import com.rav.raverp.data.model.api.ForgotApiResponse;
import com.rav.raverp.data.model.api.GenderModel;
import com.rav.raverp.data.model.api.GetBlockModel;
import com.rav.raverp.data.model.api.GetProfileModel;
import com.rav.raverp.data.model.api.GetProjectModel;
import com.rav.raverp.data.model.api.GetWalletListModel;
import com.rav.raverp.data.model.api.IFSCCodeModel;
import com.rav.raverp.data.model.api.IsActiveStatusModel;
import com.rav.raverp.data.model.api.LeadListModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.MyGoalListModel;
import com.rav.raverp.data.model.api.PaymentGatewayModel;
import com.rav.raverp.data.model.api.PaymentModeTypeModel;
import com.rav.raverp.data.model.api.PendingClosedTicketModel;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.data.model.api.PlotBooking;
import com.rav.raverp.data.model.api.PlotBookingPlan;
import com.rav.raverp.data.model.api.PlotBookingStatus;
import com.rav.raverp.data.model.api.PlotCostModal;
import com.rav.raverp.data.model.api.ReceiptModel;
import com.rav.raverp.data.model.api.ResetPasswordModel;
import com.rav.raverp.data.model.api.SendOtpForPlotBookingModel;
import com.rav.raverp.data.model.api.SiteVisitRequestModel;
import com.rav.raverp.data.model.api.SiteVisitRequestName;
import com.rav.raverp.data.model.api.SiteVisitRequestStatusModel;
import com.rav.raverp.data.model.api.SubjectModel;
import com.rav.raverp.data.model.api.WalletAccessResponse;
import com.rav.raverp.data.model.api.WalletAmountListModel;
import com.rav.raverp.data.model.local.ChangePasswordModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiHelper {

    //Login
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.LOGIN)
    Call<ApiResponse<List<LoginModel>>> userLogin(@Query("LoginId") String LoginId,
                                                  @Query("Password") String Password);

    //AvailablePlot
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.AvailablePlot)
    Call<ApiResponse<List<PlotAvailableModel>>> getPlotAvailable();

    //ChangePassword
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.ChangePassword)
    Call<ApiResponse<List<ChangePasswordModel>>> UpdateChangePassword(@Query("LoginId") String LoginId,
                                                                      @Query("OldPassword") String Password,
                                                                      @Query("NewPassword") String NewPassword,
                                                                      @Query("ConfirmPassword") String ConfirmPassword,
                                                                      @Query("RoleId") Integer RoleId);

    //GetProject
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.GetProject)
    Call<ApiResponse<List<GetProjectModel>>> getProject();

    //GetBlocks
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetBlocks)
    Call<ApiResponse<List<GetBlockModel>>> getBlocks(@Query("projectId") String projectId);

    //PlotAvailability
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.PlotAvailability)
    Call<ApiResponse<List<PlotAvailableModel>>> getPlotAvailabilitylistfilter(@Query("projectId") int projectId,
                                                                              @Query("blockId") int blockId,
                                                                              @Query("Start") int Start,
                                                                              @Query("End") int End);

    //ChangeMobileNo
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.ChangeMobileNo)
    Call<EditMobileModel> getEditMobile(@Query("loginId") String loginId,
                                        @Query("MobileNo") String MobileNo,
                                        @Query("RoleId") Integer RoleId);

    //ChangeEmailId
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.ChangeEmailId)
    Call<EditEmailModel> getEditEmail(@Query("loginId") String loginId,
                                      @Query("EmailId") String EmailId,
                                      @Query("RoleId") Integer RoleId);

    //Profile
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.Profile)
    Call<ApiResponse<List<GetProfileModel>>> getProfile(@Query("LoginId") String LoginId,
                                                        @Query("RoleId") Integer RoleId);

    //GetMyGoal
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetMyGoal)
    Call<ApiResponse<List<MyGoalListModel>>> getMyGoalListModel(@Query("LoginId") String LoginId,
                                                                @Query("roleId") Integer roleId);

    //GetAllLead
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.GetAllLead)
    Call<ApiResponse<List<LeadListModel>>> getLeadListModel();

    //GetLead
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetLead)
    Call<ApiResponse<List<LeadListModel>>> getGoLeadListModel(@Query("requesterName") String requesterName);

    //GetAllCustomer
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.GetAllCustomer)
    Call<ApiResponse<List<CustomerListModel>>> getCustomerListModel();

    //GetCustomer
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetCustomer)
    Call<ApiResponse<List<CustomerListModel>>> getGoCustomerListModel(@Query("Name") String requesterName,
                                                                      @Query("EmailId") String EmailId,
                                                                      @Query("Mobile") String Mobile);

    //GetAllLeadFollowUp
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.GetAllLeadFollowUP)
    Call<ApiResponse<List<FollowUpListModel>>> getFollowUpListModel();

    //GetLeadFollowUp
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetLeadFollowUP)
    Call<ApiResponse<List<FollowUpListModel>>> getGoFollowUpListModel(@Query("requesterName") String requesterName);

    //FollowUp
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.FollowUp)
    Call<FollowUpModel> getFollowUpModel(@Query("LoginId") String LoginId,
                                         @Query("bigIntLeadId") String bigIntLeadId,
                                         @Query("NoteFollowUp") String NoteFollowUp,
                                         @Query("FollowUpDate") String FollowUpDate);


    //GetSiteVisit
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetSiteVisit)
    Call<ApiResponse<List<SiteVisitRequestModel>>> getSiteVisitRequestModel(@Query("RequesterName") String RequesterName);


    //GetSIteVisitRequestName
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.GetSiteVisitRequesterName)
    Call<ApiResponse<List<SiteVisitRequestName>>> getSiteVisitRequestName();

    //GetWallet
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetWallet)
    Call<ApiResponse<List<GetWalletListModel>>> getGetWalletListModel(@Query("strPaymentType") String strPaymentType,
                                                                      @Query("LoginId") String LoginId,
                                                                      @Query("RoleId") int RoleId);

    //SiteRequestList
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.SiteVisitRequestList)
    Call<ApiResponse<List<SiteVisitRequestStatusModel>>> getSiteVisitRequestStatusModel(@Query("RequesterName") String RequesterName,
                                                                                        @Query("mobileNo") String mobileNo);

    //GenerateAssociateWalletpin
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GenerateAssociateWalletPins)
    Call<ApiResponse> getGenerateAssociateWalletPins(@Query("AssociateLoginId") String AssociateLoginId,
                                                     @Query("WalletPin") String WalletPin,
                                                     @Query("RoleId") int RoleId);

    //LoginAssociateWallet
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.LoginAssociateWallet)
    Call<ApiResponse> getLoginAssociateWallet(@Query("AssociateLoginId") String AssociateLoginId,
                                              @Query("WalletPin") String WalletPin,
                                              @Query("RoleId") int RoleId);

    //WalletPinAccess
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.WalletPinAccess)
    Call<WalletAccessResponse> getWalletPinAccess(@Query("AssociateLoginId") String AssociateLoginId,
                                                  @Query("RoleId") int RoleId);

    //GetRPalantinumWalletAmount
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetRPalantinumWalletAmount)
    Call<ApiResponse<List<WalletAmountListModel>>> getWalletAmountListModel(@Query("AssociateLoginId") String AssociateLoginId,
                                                                            @Query("RoleId") int RoleId);

    //GetCustomer_Plot_book
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetCustomer_Plot_book)
    Call<ApiResponse<List<CustomerPlotBookModel>>> getCustomerPlotBookModel(@Query("AssociateLoginId") String AssociateLoginId,
                                                                            @Query("RoleId") int RoleId);

    //GetAssociateEWalletAmount
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetAssociateEWalletAmount)
    Call<AssociateWalletAmount> getAssociateWalletAmountModel(@Query("AssociateLoginId") String AssociateLoginId,
                                                              @Query("RoleId") int RoleId);

    //GetCustomerAmount
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetCustomerAmount)
    Call<CustomerAmount> getCustomerAmountModel(@Query("AssociateLoginId") String AssociateLoginId,
                                                @Query("RoleId") int RoleId,
                                                @Query("bigIntCustomerId") int bigIntCustomerId);

    //PlotBookingStatus
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.PlotBookingStatus)
    Call<ApiResponse<List<PlotBookingStatus>>> getPlotBookingStatus();

    //PlotBookingPlan
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.PlotBookingPlan)
    Call<ApiResponse<List<PlotBookingPlan>>> getPlotBookingPlan();

    //AssociatePlotBookings
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.AssociatePlotBookings)
    Call<PlotBooking> getPlotBooking(@Query("AssociateLoginId") String AssociateLoginId,
                                     @Query("RoleId") int RoleId,
                                     @Query("plotId") String plotId,
                                     @Query("intPlotBookedStatusId") int intPlotBookedStatusId,
                                     //@Query("bigIntCustomerId")int bigIntCustomerId,
                                     @Query("intPlanType") int intPlanType,
                                     @Query("dcPlotBooingAmount") String dcPlotBooingAmount,
                                     @Query("intPlotEMIMonth") String intPlotEMIMonth,
                                     @Query("intDuration") String intDuration,
                                     @Query("OTPid") String OTPid,
                                     @Query("OTP") String OTP);

    //IsActiveStatus
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.IsActiveStatus)
    Call<ApiResponse<List<IsActiveStatusModel>>> getIsActiveStatusModel();


    //AddEditCustomer
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.AddEditCustomer)
    Call<AddCustomerEditCustomer> getAddCustomerEditCustomerModel(@Query("AssociateLoginId") String AssociateLoginId,
                                                                  @Query("RoleId") int RoleId,
                                                                  @Query("bigIntCustomerId") int bigIntCustomerId,
                                                                  @Query("strName") String strName,
                                                                  @Query("strEmail") String strEmail,
                                                                  @Query("strMobileNo") String strMobileNo,
                                                                  @Query("strPinCode") String strPinCode,
                                                                  @Query("strStateName") String strStateName,
                                                                  @Query("strCityName") String strCityName,
                                                                  @Query("strAddress") String strAddress,
                                                                  @Query("intIsDelated") int intIsDelated,
                                                                  @Query("intGenderId") int intGenderId);

    //GetGender
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.GetGender)
    Call<ApiResponse<List<GenderModel>>> getGenderModel();

    //DeleteCustomer
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.DeleteCustomer)
    Call<DeleteCustomerModel> getDeleteCustomerModel(@Query("AssociateLoginId") String AssociateLoginId,
                                                     @Query("custId") int custId);

    //CustomerPlotBookingDetals
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.CustomerPlotBookingDetals)
    Call<ApiResponse<List<CustomerPlotDetails>>> getCustomerPlotDetailsModel(@Query("AssociateLoginId") String AssociateLoginId,
                                                                             @Query("roleId") int roleId,
                                                                             @Query("CustomerId") int CustomerId);


    //ForgetPassword
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.ForgetPassword)
    Call<ForgotApiResponse> getForgotpasswordModel(@Query("Uid") String Uid);

    //UserChangePassword
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.UserChangePassword)
    Call<ResetPasswordModel> getResetPasswordModel(@Query("userLoginId") String userLoginId,
                                                   @Query("npass") String npass);

    //ChangeUserProfile
    @Multipart
    @POST(ApiEndPoint.ChangeUserProfile)
    Call<ApiUploadImageResponse> getChangeProfile(@Query("userName") String key,
                                                  @Query("loginId") String requestBody2,
                                                  @Part MultipartBody.Part screenshot);

    //Activity
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.Activity)
    Call<ApiResponse<List<ActivityLogModel>>> getActivityLogModel(@Query("userLoginId") String userLoginId);

    //CustomerPlotBookingDetails
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.CustomerPlotBookingDetails)
    Call<ApiResponse<List<CustomerPlotDetailsModel>>> getCustomerPlotDetailsModel(@Query("customerloginId") String customerloginId);

    //CustomerPlotBookingAccountDetails
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.CustomerPlotBookingAccountDetails)
    Call<ApiResponse<List<CustomerPlotBookingAccountDetailsModel>>> getCustomerPlotBookingAccountDetailsModel(@Query("customerloginId") String customerloginId);

    //Paymentgateway
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.Paymentgateway)
    Call<ApiResponse<PaymentGatewayModel>> getPaymentGatewayModel(@Query("AssociateLoginId") String AssociateLoginId,
                                                                  @Query("RoleId") int RoleId,
                                                                  @Query("dcAmount") String dcAmount,
                                                                  @Query("strPaymentType") String strPaymentType,
                                                                  @Query("CustId") int CustId);

    /*---------------------------------------------------------------Aditya---------------------------------------------------------------*/

    //AssociatePlotBookingDetails
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.AssociatePlotBookingDetails)
    Call<ApiResponse<List<CustomerPlotDetails>>> getAssociatePlot(@Query("AssociateLoginId") String AssociateLoginId,
                                                                  @Query("RoleId") int RoleId);

    //WalletPayment
    @Multipart
    @POST(ApiEndPoint.WalletPayment)
    Call<AddCustomerEditCustomer> getPayment(@Query("AssociateLoginId") String AssociateLoginId,
                                             @Query("RoleId") int RoleId,
                                             @Query("dcAmount") String dcAmount,
                                             @Query("strPaymentType") String strPaymentType,
                                             @Query("strNFTNo") String strNFTNo,
                                             @Query("strUTRNo") String strUTRNo,
                                             @Query("slipname") String slipname,
                                             @Query("txnid") String txnid,
                                             @Query("payuMoneyId") String payuMoneyId,
                                             @Query("status") String status,
                                             @Query("phone") String phone,
                                             @Query("email") String email,
                                             @Part MultipartBody.Part slip);

    //WalletPayment
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.WalletPayment)
    Call<AddCustomerEditCustomer> getPaymentOnline(@Query("AssociateLoginId") String AssociateLoginId,
                                                   @Query("RoleId") int RoleId,
                                                   @Query("dcAmount") String dcAmount,
                                                   @Query("strPaymentType") String strPaymentType,
                                                   @Query("strNFTNo") String strNFTNo,
                                                   @Query("strUTRNo") String strUTRNo,
                                                   @Query("slipname") String slipname,
                                                   @Query("txnid") String txnid,
                                                   @Query("payuMoneyId") String payuMoneyId,
                                                   @Query("status") String status,
                                                   @Query("phone") String phone,
                                                   @Query("email") String email);

    //MemberRegistration
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.MemberRegistration)
    Call<AddAssociateModal> getAssociate(@Query("MemberLoginId") String MemberLoginId,
                                         @Query("SponsorId") String SponsorId,
                                         @Query("DisplayName") String DisplayName,
                                         @Query("Leg") String Leg,
                                         @Query("Gender") String Gender,
                                         @Query("Mobile") String Mobile,
                                         @Query("Email") String Email,
                                         @Query("DOB") String DOB,
                                         @Query("Password") String Password,
                                         @Query("Relation") String Relation,
                                         @Query("FatherName") String FatherName,
                                         @Query("PinCode") String PinCode,
                                         @Query("StateName") String StateName,
                                         @Query("CityName") String CityName,
                                         @Query("Address") String Address);

    //GetDashBoardAssociateCalc
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.GetDashBoardAssociateCalc)
    Call<DashBoardModal> getDashboard(@Query("AssociateLoginId") String AssociateLoginId,
                                      @Query("RoleId") int RoleId);

    //PlotReceipt
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.PlotReceipt)
    Call<BookingPlotListModal> getBookingPlotList(@Query("AssociateLoginId") String AssociateLoginId,
                                                  @Query("RoleId") int RoleId);

    //PlotReceiptDetails
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.PlotReceiptDetails)
    Call<ReceiptModel> getReceiptList(@Query("AssociateLoginId") String AssociateLoginId,
                                      @Query("RoleId") int RoleId,
                                      @Query("bigIntBookingId") String bigIntBookingId);

    //PlotCalculationOnType
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.PlotCalculationOnType)
    Call<PlotCostModal> getPlotCost(@Query("AssociateLoginId") String AssociateLoginId,
                                    @Query("RoleId") int RoleId,
                                    @Query("bigIntPlotId") String bigIntPlotId,
                                    @Query("month") String month);

    //SendTransactionOTP
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.SendTransactionOTP)
    Call<SendOtpForPlotBookingModel> getOtp(@Query("AssociateLoginId") String AssociateLoginId,
                                            @Query("RoleId") int RoleId);


    //ForgotWalletPin
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.ForgotWalletPin)
    Call<AddCustomerEditCustomer> getForgotWalletPin(@Query("AssociateLoginId") String AssociateLoginId,
                                                     @Query("RoleId") int RoleId);


    //ValidateWalletPinOTP
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.ValidateWalletPinOTP)
    Call<AddCustomerEditCustomer> validateWalletPin(@Query("AssociateLoginId") String AssociateLoginId,
                                                    @Query("RoleId") int RoleId,
                                                    @Query("otp") String otp);

    //LoginAssociateWalletOtp
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.LoginAssociateWalletOtp)
    Call<AddCustomerEditCustomer> loginWalletOtp(@Query("AssociateLoginId") String AssociateLoginId,
                                                 @Query("WalletPin") String WalletPin,
                                                 @Query("RoleId") int RoleId);

    //VerifyLoginAssociateWalletOtp
    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.VerifyLoginAssociateWalletOtp)
    Call<AddCustomerEditCustomer> verifyLoginWalletOtp(@Query("AssociateLoginId") String AssociateLoginId,
                                                       @Query("OTP") String OTP,
                                                       @Query("RoleId") int RoleId);

    //GetSubject
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.Subject)
    Call<ApiResponse<List<SubjectModel>>> getSubject();

    //GetClaimType
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.ClaimType)
    Call<ApiResponse<List<ClaimTypeModel>>> getClaim();

    //GetDocumentType
    @Headers({"Content-Type:application/json"})
    @GET(ApiEndPoint.Documents)
    Call<ApiResponse<List<DocumentTypeModel>>> getDocument();

    //GetPaymentMode Type
    @Headers({"Content-Type:application/json"})
    @GET(ApiEndPoint.PaymentModeType)
    Call<ApiResponse<List<PaymentModeTypeModel>>> getPaymentMode();

    //GetIFSC Code
    @GET(ApiEndPoint.IFSCCode + "{ifsccode}")
    Call<IFSCCodeModel> getIFSCCode(@Path("ifsccode") String ifsccode);


    //WalletPayment
    @Multipart
    @POST(ApiEndPoint.CreateTicket)
    Call<CommonModel> createTicket(@Query("AssociateLoginId") String AssociateLoginId,
                                   @Query("RoleId") int RoleId,
                                   @Query("SubjectId") int SubjectId,
                                   @Query("ClaimType") int ClaimType,
                                   @Query("intDocumentTypeId") int intDocumentTypeId,
                                   @Query("strPaymentType") String strPaymentType,
                                   @Query("strTransactionNo") String strTransactionNo,
                                   @Query("dcAmount") String dcAmount,
                                   @Query("dtTransDate") String dtTransDate,
                                   @Query("IFSC") String IFSC,
                                   @Query("strBranchName") String strBranchName,
                                   @Query("strBankName") String strBankName,
                                   @Query("Query") String Query,
                                   @Part MultipartBody.Part slip);

    //GetPending Ticket List
    @Headers({"Content-Type:application/json"})
    @POST(ApiEndPoint.PendingTickets)
    Call<PendingClosedTicketModel> getPendingTicket(@Query("AssociateLoginId") String AssociateLoginId,
                                                    @Query("RoleId") int RoleId,
                                                    @Query("TicketNo") String TicketNo,
                                                    @Query("active") boolean active);

    //Get Closed Ticket List
    @Headers({"Content-Type:application/json"})
    @POST(ApiEndPoint.ClosedTickets)
    Call<PendingClosedTicketModel> getClosedTicket(@Query("AssociateLoginId") String AssociateLoginId,
                                                   @Query("RoleId") int RoleId);


    //Chat List
    @Headers({"Content-Type:application/json"})
    @POST(ApiEndPoint.ViewTickets)
    Call<ChatModel> getChatList(@Query("AssociateLoginId") String AssociateLoginId,
                                @Query("RoleId") int RoleId,
                                @Query("TicketNo") String TicketNo,
                                @Query("active") boolean active);


    //Chat List
    @Headers({"Content-Type:application/json"})
    @POST(ApiEndPoint.MsgSend)
    Call<ChatModel> sendMsg(@Query("AssociateLoginId") String AssociateLoginId,
                                @Query("RoleId") int RoleId,
                                @Query("TicketNo") String TicketNo,
                                @Query("Msg") String Msg);
}
