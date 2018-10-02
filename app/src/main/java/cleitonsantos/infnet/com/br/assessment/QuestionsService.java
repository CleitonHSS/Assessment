package cleitonsantos.infnet.com.br.assessment;

import android.content.ClipData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QuestionsService {

        @GET("/questions?order=desc&sort=activity&tagged={tag}&site=stackoverflow")
        Call<List<Perguntas>> buscarPerguntas(@Path("tag") String tag);
    }
