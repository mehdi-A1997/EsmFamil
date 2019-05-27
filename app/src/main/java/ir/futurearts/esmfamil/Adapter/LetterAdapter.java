package ir.futurearts.esmfamil.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.futurearts.esmfamil.Interface.SelectLetterInterface;
import ir.futurearts.esmfamil.Module.LetterM;
import ir.futurearts.esmfamil.R;

public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.myHolder> {

    private List<LetterM> data;
    private Context context;
    private SelectLetterInterface sli;

    public ConstraintLayout lastSelected= null;

    public LetterAdapter(List<LetterM> data, Context context, SelectLetterInterface sli) {
        this.data = data;
        this.context = context;
        this.sli = sli;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_letter,parent,false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        LetterM l= data.get(position);
        holder.set(l, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myHolder extends RecyclerView.ViewHolder{
        private TextView letter;
        private View v;
        private ConstraintLayout sec;
         myHolder(@NonNull View itemView) {
            super(itemView);

            v= itemView;
            sec= itemView.findViewById(R.id.letter_sec);
            letter= itemView.findViewById(R.id.letter_text);
        }

         void set(final LetterM l, int position) {
             letter.setText(l.getLetter());

             v.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(lastSelected==null)
                     {
                         lastSelected= sec;
                     }
                     lastSelected.setBackground(ContextCompat.getDrawable(context, R.drawable.letter_bg));
                     lastSelected= sec;
                     lastSelected.setBackground(ContextCompat.getDrawable(context, R.drawable.letter_selected_bg));

                     sli.letterSelected(l);
                 }
             });
        }
    }
}
