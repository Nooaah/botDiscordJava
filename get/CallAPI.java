/**
 * CallAPI
 */
public class CallAPI {

    public static void main(String[] args) {
        Get Call = new Get("https://jsonplaceholder.typicode.com/todos/");
        Call.CallAPI();
    }
}