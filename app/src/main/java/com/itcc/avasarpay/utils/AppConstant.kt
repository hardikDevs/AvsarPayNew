package com.itcc.avasarpay.utils



object AppConstant {
    const val DATA ="data"
    const val EMAIL ="email"
    const val DATA_ITEM_LIST ="data_item_list"
    const val DATA_DRAWING_LIST ="data_drawing_list"
    const val DATA_ADDITIONAL_LIST ="data_additional_list"
    const val DATA_MEASUREMENT_LIST ="data_measurement_list"
    const val DATA_ID ="data_id"
    const val JOB_ID ="job_id"
    const val TITLE ="title"
    const val DEBOUNCE_TIMEOUT = 300L
    const val MIN_SEARCH_CHAR = 3
    const val INITIAL_PAGE = 1
    const val PAGE_SIZE = 10

    const val MEASUREMENT_DATA="measurement_data"
    const val DRAWING_DATA="drawing_data"
    const val QUOTATION_DATA="quotation_data"
    const val LEAD_DATA="lead_data"
    const val ATTACHMENT_DATA="attachment_data"
    const val ATTACHMENT_PHOTO_CURRENT="attachment_photo_current"
    const val ATTACHMENT_PHOTO_COMPLETED="attachment_photo_completed"



    // ---Server Date Time--//
    const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    //---- Local Date Time---//
   const val DATE_FORMAT_DD_MM_YY = "dd/MM/yyyy HH:mm:ss"

    /**
     * Times in long
     * */
    const val CONNECT_TIMEOUT = 70
    const val READ_TIMEOUT = 70

    var LAST_CLICK_ITEM: Long = 0
    const val MULTIPLE_CLICK_THRESHOLD = 800


    // Font Path
    const val REGULAR_FONT ="font/Poppins_Regular.ttf"
    const val SEMI_BOLD_FONT ="font/Poppins_SemiBold.ttf"
    const val MEDIUM_FONT ="font/Poppins_Medium.ttf"

    //Roles
    const val ROLE_ADMIN =1
    const val ROLE_SALES =5
    const val ROLE_INSTALLATION =4
    const val ROLE_MANUFACTURING =3
    const val ROLE_MEASUREMENT =2

    // Quotation Status
    const val DRAFT             = 1
    const val SENT              = 2
    const val QUOTATION_FOLLOW_UP  = 3
    const val ACCEPTED          = 4
    const val DECLINED          = 5
    const val CONVERTED_TO_JOB  = 6
    const val EXPIRED  = 7


    // Job Status
    const val MEASUREMENTS  = 1
    const val MANUFACTURING = 2
    const val INSTALLATION  = 3
    const val BALANCE_PAYMENT = 4
    const val COMPLETED = 5

    // Payment Status
    const val PAYMENT_PENDING   = 1
    const val PAID_IN_FULL      = 2



    // Lead Status
    const val ALL_LEAD      = 0
    const val NEW_LEAD      = 1
    const val LEAD_FOLLOW_UP = 2
    const val NOT_INTERESTED= 3
    const val INTERESTED    = 4
    const val OTHER         = 5
    const val CONVERT_TO_QUOTE = 6

    // Click Action
    const val DESCRIPTION_CLICK ="Desc"
    const val SEND_QUOTE_CLICK ="SendQuote"
    const val DELETE_CLICK ="Delete"
    const val LEAD_CONVERT_CLICK ="LeadConvertToQuote"
    const val QUOTE_CONVERT_CLICK ="QuoteConvertToQuote"

    // Event Category
    const val EVENT_CATEGORY_MARRIAGE = 1
    const val EVENT_CATEGORY_BIRTHDAY = 2
    const val EVENT_CATEGORY_ANNIVERSARY = 3
    const val EVENT_CATEGORY_BABY_SHOWER = 4


}