package cleitonsantos.infnet.com.br.assessment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.ContatosListHolder> {

    private List<String> perguntasList;

    public ContatosAdapter(List<String> perguntasList) {
        this.perguntasList = perguntasList;
    }

    @NonNull
    @Override
    public ContatosListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.contato_card;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ContatosListHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContatosListHolder holder, int position) {
        if (perguntasList != null && position < perguntasList.size()) {
            String perguntas = perguntasList.get(position);
            holder.nome.setText(perguntas);
        }
    }

    @Override
    public int getItemCount() {
        return perguntasList.size();
    }

    class ContatosListHolder extends RecyclerView.ViewHolder {

        public TextView nome;



        ContatosListHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nome);

            itemView.setOnClickListener(new View.OnClickListener() {
                boolean opencard = true;
                @Override
                public void onClick(View view) {
                    LinearLayout lview = view.findViewById(R.id.l_view);
                    if (opencard){
                        lview.setVisibility(view.VISIBLE);
                    } else {
                        lview.setVisibility(view.GONE);
                    }
                    opencard = !opencard;
                }
            });

        }
    }
}
