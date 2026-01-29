package tw.com.tbb.central.constants;

public class ErrorCodeConstants {
    public static final String SUCCESS = "0000";//成功
    public static final String E200 = "E200";//欄位不可為空白
    public static final String E201 = "E201";//欄位不可為空值
    public static final String E202 = "E202";//欄位格式錯誤
    public static final String E250 = "E250"; // 轉出帳號不存在
    public static final String E251 = "E251"; // 轉入帳號不存在
    public static final String E252 = "E252"; // 餘額不足
    public static final String E253 = "E253"; // 金額需 > 0
    public static final String E254 = "E254"; // 密碼錯誤
    public static final String E255 = "E255"; // 帳號不存在
    public static final String E256 = "E256"; // 查無帳號
    public static final String E257 = "E257"; // 查無約定帳號
    public static final String JSON_ERROR = "E900";//請求 JSON 反序列化失敗
    public static final String SYS_ERROR = "E999";//系統錯誤
}
