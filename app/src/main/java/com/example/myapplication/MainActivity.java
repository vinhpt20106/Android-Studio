package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ExpenseAdapter;
import com.example.myapplication.model.Expense;
import com.example.myapplication.utils.CurrencyUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private List<Expense> expenseList;
    private ExpenseAdapter expenseAdapter;
    private TextView tvTotalAmount;

    private ActivityResultLauncher<Intent> addExpenseLauncher;
    private ActivityResultLauncher<Intent> detailExpenseLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        RecyclerView recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
        Button btnAddExpense = findViewById(R.id.btnAddExpense);

        expenseList = new ArrayList<>();
        loadInitialData();

        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(expenseList, (expense, position) -> {
            Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
            intent.putExtra("expense_item", expense);
            intent.putExtra("expense_position", position);
            detailExpenseLauncher.launch(intent);
        });
        recyclerViewExpenses.setAdapter(expenseAdapter);

        updateTotalAmount();

        // Activity Result Launcher for Add Expense
        addExpenseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Expense newExpense = (Expense) result.getData().getSerializableExtra("new_expense");
                        if (newExpense != null) {
                            expenseList.add(0, newExpense);
                            expenseAdapter.notifyItemInserted(0);
                            recyclerViewExpenses.scrollToPosition(0);
                            updateTotalAmount();
                        }
                    }
                }
        );

        // Activity Result Launcher for Expense Detail (Delete)
        detailExpenseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        int deletePosition = result.getData().getIntExtra("delete_position", -1);
                        if (deletePosition >= 0 && deletePosition < expenseList.size()) {
                            expenseList.remove(deletePosition);
                            expenseAdapter.notifyItemRemoved(deletePosition);
                            updateTotalAmount();
                        }
                    }
                }
        );

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            addExpenseLauncher.launch(intent);
        });
    }

    private void loadInitialData() {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        expenseList.add(new Expense("1", "Ăn sáng", 30000, "Ăn uống", currentDate, "Ăn sáng"));
        expenseList.add(new Expense("2", "Đổ xăng", 50000, "Đi lại", currentDate, "Đổ xăng"));
        expenseList.add(new Expense("3", "Mua sách", 120000, "Học tập", currentDate, "Sách Lập trình Android"));
    }

    private void updateTotalAmount() {
        long total = 0;
        for (Expense expense : expenseList) {
            total += expense.getAmount();
        }
        tvTotalAmount.setText(CurrencyUtils.formatVnd(total));
    }
}
