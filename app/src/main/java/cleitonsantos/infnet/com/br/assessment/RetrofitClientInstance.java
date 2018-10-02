package cleitonsantos.infnet.com.br.assessment;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
        private static Retrofit retrofit;
        private static final String BASE_URL = "https://api.stackexchange.com/2.2/";
//    private static final String BASE_URL = "http://infnet.educacao.ws/dadosAtividades.php"; // api offline

        public static Retrofit getInstance() {
            if (retrofit == null) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
}
