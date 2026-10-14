package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Expense;
import com.example.myapplication.utils.CurrencyUtils;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final List<Expense> expenseList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Expense expense, int position);
    }

    public ExpenseAdapter(List<Expense> expenseList, OnItemClickListener listener) {
        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvTitle.setText(expense.getTitle());
        holder.tvAmount.setText(CurrencyUtils.formatVnd(expense.getAmount()));
        holder.tvCategory.setText(expense.getCategory());
        holder.tvDate.setText(expense.getDate());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(expense, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAmount, tvCategory, tvDate;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvExpenseTitle);
            tvAmount = itemView.findViewById(R.id.tvExpenseAmount);
            tvCategory = itemView.findViewById(R.id.tvExpenseCategory);
            tvDate = itemView.findViewById(R.id.tvExpenseDate);
        }
    }
}
