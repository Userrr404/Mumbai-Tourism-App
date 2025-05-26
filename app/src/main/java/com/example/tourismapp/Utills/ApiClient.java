package com.example.tourismapp.Utills;

import java.security.PublicKey;

public class ApiClient {

    public static final String USER_LOGIN = "http://10.0.2.2/tourism/db_verify_login.php";
    public static final String USER_REGISTER = "http://10.0.2.2/tourism/db_insert_reg.php";
    public static final String DISPLAY_PLACES_URL = "http://10.0.2.2/tourism/DB_display_tourist_places.php";
    public static final String SEARCH_PLACES_URL = "http://10.0.2.2/tourism/DB_search_user_places.php";
    public static final String GET_USER_SAVE_PLACES_URL = "http://10.0.2.2/tourism/TB_get_user_saved_places.php?";
    public static final String SHORT_URL = "http://10.0.2.2/tourism/";
    public static final String UPDATE_USER_PROFILE = "http://10.0.2.2/tourism/db_updateProfile_pro.php";
    public static final String UPDATE_USER_EMAIL = "http://10.0.2.2/tourism/db_updateEmail_pro.php";
    public static final String UPDATE_USER_PASSWORD = "http://10.0.2.2/tourism/db_updatePassword_pro.php";
    public static final String SAVE_PLACE_URL = "http://10.0.2.2/tourism/TB_user_save_places.php";
    public static final String REMOVE_PLACE_URL = "http://10.0.2.2/tourism/TB_user_remove_place.php";
    public static final String ADD_PLACE_URL = "http://10.0.2.2/tourism/db_add_places.php";
    public static final String UPDATE_PLACE_URL = "http://10.0.2.2/tourism/db_update_place.php";
    public static final String ADD_BOOKING_URL = "http://10.0.2.2/tourism/db_insert_booking.php";
    public static final String DELETE_BOOKING_URL = "http://10.0.2.2/tourism/db_delete_booking.php";
    public static final String UPDATE_BOOKING_URL = "http://10.0.2.2/tourism/db_update_booking_status.php";
    public static final String VIEW_ALL_BOOKING = "http://10.0.2.2/tourism/db_fetch_all_bookings.php";
    public static final String VIEW_ALL_SIGNUP_USER_URL = "http://10.0.2.2/tourism/db_get_all_users.php";
    public static final String VIEW_ALL_FEEDBACK = "http://10.0.2.2/tourism/db_fetch_feedback.php";
    public static final String SUBMIT_FEEDBACK = "http://10.0.2.2/tourism/db_submit_feedback.php";
    public static final String ADMIN_SIGNUP_URL = "http://10.0.2.2/tourism/db_admin_signup.php";
    public static final String ADMIN_LOGIN_URL = "http://10.0.2.2/tourism/db_admin_login.php";
    public static final String GET_ITINERARY_URL = "http://10.0.2.2/tourism/get_itinerary.php";
    // Base URL for image loading (assuming images are stored in htdocs/uploads/)
    public static final String IMAGE_BASE_URL = "http://10.0.2.2/tourism/";

    public static final String FILTER_BY_CATEGORY_URL = "http://10.0.2.2/tourism/db_filter_places.php";

}
