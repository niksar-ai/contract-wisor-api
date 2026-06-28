package ai.niksar.contract_wisor_api.util;

public class Constants {
    private Constants(){}

    public static final String BATCH_BASE_PATH = "ai.niksar.contract_wisor_api.service.";

    //region All Tables
    public static final class Tables {
        public static final String DOCUMENT="document";
        public static final String DOCUMENT_METADATA="document_metadata";
        public static final String DOCUMENT_TYPE="document_type";
        public static final String DOCUMENT_RELATIONS="document_relations";
        public static final String DOCUMENT_VIEW="document_views";
        public static final String DOCUMENT_TREE = "documenttree";
        public static final String USERS = "user";
        public static final String SESSION_HISTORY = "session_history";
        public static final String DOCUMENT_COMMENT= "document_comments";
        public static final String DOCUMENT_FAVORITE = "favorite_documents";
        public static final String MENU = "menu";
        public static final String ROLE = "role";
        public static final String ROLE_MENUS = "role_menu_permission";
        public static final String SESSION = "session";
        public static final String USER_ROLES = "role_user_permission";
        public static final String BATCH_DEFINITION = "batch_definition";
        public static final String BATCH_LOG = "batch_log";
        public static final String REFERENCE_DATA_CONTEXT = "reference_data_context";
        public static final String REFERENCE_DATA = "reference_data";

    }
    //endregion

    //region User Table Constants
    public static final class UserTable{
        public static final String EMAIL="email";
        public static final String NAME="name";
        public static final String SURNAME= "surname";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String NAME_TITLE = "name_title";
        public static final String CREATE_DATE = "create_date";
        public static final String CREATE_TIME = "create_time";
        public static final String LAST_LOGIN_DATE = "last_login_date";
        public static final String LAST_LOGIN_TIME = "last_login_time";
        public static final String LAST_FAILURE_LOGIN_TIME = "last_failure_login_time";
        public static final String STATUS = "status";
        public static final String AVATAR = "avatar";
        public static final String IS_PASSWORD_CHANGE = "is_password_change";
        public static final String PASSWORD_EXPIRY_MONTH = "password_expiry_month";
    }
    //endregion

    public static class BatchDefinitionTable {
        public static final String SERVICE_NAME = "service_name";
        public static final String BATCH_NAME = "batch_name";
        public static final String CRON_TIME = "cron_time";
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
        public static final String STATUS = "status";
        public static final String MAX_RETRY_COUNT = "max_retry_count";
        public static final String RETRY_INTERVAL = "retry_interval";
    }

    public static class BatchLogTable {
        public static final String ID = "id";
        public static final String BATCH_ID = "batch_id";
        public static final String STATUS = "status";
        public static final String ERROR_MESSAGE = "error_message";
        public static final String LOG_TIME = "log_time";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String DURATION = "duration";
    }

    //region Token Constants
    public static final class Jwt{
        public static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 15; //15 minutes
        public static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 15; // 15 days
    }
    //endregion

    //region Status Constants
    public static final class Status {
        public static final String ACTIVE = "1";
        public static final String PASSIVE = "0";
    }
    //endregion

    public static final class BatchLogStatus{
        public static final String PROCESSING = "1";
        public static final String SUCCESS = "2";
        public static final String FAILURE = "3";
    }

    //region LoginStatus Constants
    public static final class LoginStatus {
        public static final String SUCCESS = "1";
        public static final String FAILURE = "0";
    }
    //endregion

    public static final class GeneralKeys {
        public static final String USERNAME                     = "username";
        public static final String PASSWORD                     = "password";
        public static final String NAME_TITLE                   = "nameTitle";
        public static final String CREATE_DATE                  = "createDate";
        public static final String CREATE_TIME                  = "createTime";
        public static final String LAST_LOGIN_DATE              = "lastLoginDate";
        public static final String LAST_LOGIN_TIME              = "lastLoginTime";
        public static final String STATUS                       = "status";
        public static final String REFRESH_TOKEN                = "refreshToken";
        public static final String ACCESS_TOKEN                 = "accessToken";
        public static final String EXPECTED                     = "expected";
        public static final String SHORT_ANSWER_TYPE            = "shortAnswerType";
        public static final String SUCCESS_RATE                 = "successRate";
        public static final String SHORT_ANSWER_VALUE           = "shortAnswerValue";
        public static final String LASTED                       = "lasted";
        public static final String SUMMARY_ANSWER               = "summaryAnswer";
        public static final String PROCESS_STARTED              = "processStarted";
        public static final String LONG_ANSWER                  = "longAnswer";
    }

    public static final class DocumentState {
        public static final String LOADED                       = "1";
        public static final String PARSED                       = "2";
        public static final String ANALYZED                     = "3";
        public static final String COMPLETED                    = "4";
    }

    public static final class ParseState {
        public static final String WAITING                      = "0";
        public static final String PROCESS_IN_PROGRESS          = "1";
        public static final String FAULTY                       = "2";
        public static final String SUCCESSFUL                   = "3";
    }

    public static final class AnalyzeState {
        public static final String WAITING                      = "0";
        public static final String PROCESS_IN_PROGRESS          = "1";
        public static final String FAULTY                       = "2";
        public static final String SUCCESSFUL                   = "3";
    }

    public static final class AnalyzeAnswerVersions {
        public static final String MAP_REDUCE                   = "map_reduce";
        public static final String FULL_DOC_OPENAI              = "full-doc-openai";
        public static final String STUFF                        = "stuff";
        public static final String EXPECTED                     = "expected";
        public static final String DEFAULT                      = "8-ServiceLlmWisorHfLlama-llama3.3-32k";
    }

    public static final class RelationTypes {
        public static final String ATTACHMENT                   = "01";
        public static final String SUB_FILE                     = "02";
    }

    public static final class ActionCodes {
        public static final String DOCUMENT_METADATA_SAVE ="DOCUMENT_METADATA_SAVE";
        public static final String DOCUMENT_RELATION_SAVE ="DOCUMENT_RELATION_SAVE";
        public static final String DOCUMENT_RELATION_DELETE ="DOCUMENT_RELATION_DELETE";
        public static final String DOCUMENT_RELATION_VIEW_TREE ="DOCUMENT_RELATION_VIEW_TREE";
        public static final String DOCUMENT_RELATION_VIEW ="DOCUMENT_RELATION_VIEW";
        public static final String ANALYZE_DELETE ="ANALYZE_DELETE";
        public static final String DOCUMENT_DELETE ="DOCUMENT_DELETE";
        public static final String DOCUMENT_PARSE ="DOCUMENT_PARSE";
        public static final String DOCUMENT_ANALYZE ="DOCUMENT_ANALYZE";
        public static final String DOCUMENT_DOWNLOAD ="DOCUMENT_DOWNLOAD";
        public static final String DOCUMENT_COMPLETE ="DOCUMENT_COMPLETE";
        public static final String DOCUMENT_REOPEN ="DOCUMENT_REOPEN";
        public static final String DOCUMENT_COMMENT_SAVE ="DOCUMENT_COMMENT_SAVE";
        public static final String DOCUMENT_COMMENT_DELETE ="DOCUMENT_COMMENT_DELETE";
    }

}
