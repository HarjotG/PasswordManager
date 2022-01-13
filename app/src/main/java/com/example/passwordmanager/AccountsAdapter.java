package com.example.passwordmanager;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {
    public static final String ACCOUNT_ID_EXTRA = "ACCOUNT_ID_EXTRA";
    public static final String ACCOUNT_UNAME_EXTRA = "ACCOUNT_UNAME_EXTRA";
    public static final String ACCOUNT_PASS_EXTRA = "ACCOUNT_PASS_EXTRA";
    public static final String ACCOUNT_EMAIL_EXTRA = "ACCOUNT_EMAIL_EXTRA";
    public static final String ACCOUNT_TAG_EXTRA = "ACCOUNT_TAG_EXTRA";
    public static final String ACCOUNT_SITE_EXTRA = "ACCOUNT_SITE_EXTRA";

    private ArrayList<Account> accounts;
    private Context context;
    

    public AccountsAdapter(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public AccountsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_account, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView, context.getApplicationContext());
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull AccountsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Account account = accounts.get(position);

        // Set item views based on your views and data model
        TextView username = holder.textUser;
        username.setText(account.username);
        TextView site = holder.textSite;
        site.setText(account.site);

    }

    public int getItemCount() {
        return accounts.size();
    }

    private boolean deleteAccount(Account account) {
        DB db = new DB(context);
        for(int i = 0; i < accounts.size(); i++) {
            if(accounts.get(i).id.equals(account.id)) {
                accounts.remove(i);
                boolean result = db.deleteAccount(account);
                if(result) notifyItemRemoved(i);
                return result;
            }
        }
        return false;
    }

    private boolean updateAccount(Account oldAcc) {
        DB db = new DB(context);
        return db.updateAccount(oldAcc, oldAcc);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener{

        public TextView textUser;
        public TextView textSite;
        public ImageButton buttonControls;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View view, Context context) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(view);

            textUser = (TextView) view.findViewById(R.id.text_dispUser);
            textSite = (TextView) view.findViewById(R.id.text_dispSite);
            buttonControls = (ImageButton) view.findViewById(R.id.button_controls);
            textUser.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    openAccount(v, v.getContext());
                }
            });
            textSite.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    openAccount(v, v.getContext());
                }
            });
            buttonControls.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    openControls(v, v.getContext());
                }
            });
        }

        public void openAccount(View view, Context context) {
            int position = getAdapterPosition();
            Account account = accounts.get(position);

            Intent intent = new Intent(context, AccountView.class);
            intent.putExtra(ACCOUNT_ID_EXTRA, account.id);
            intent.putExtra(ACCOUNT_UNAME_EXTRA, account.username);
            intent.putExtra(ACCOUNT_PASS_EXTRA, account.password);
            intent.putExtra(ACCOUNT_EMAIL_EXTRA, account.email);
            intent.putExtra(ACCOUNT_TAG_EXTRA, account.tag);
            intent.putExtra(ACCOUNT_SITE_EXTRA, account.site);
            context.startActivity(intent);
        }

        public void openControls(View view, Context context) {
            PopupMenu popup = new PopupMenu(context, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.account_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.account_delete:
                    return deleteAccount(accounts.get(getAdapterPosition()));
                case R.id.account_update:
                    return updateAccount(accounts.get(getAdapterPosition()));
                default:
                    return false;
            }

        }

    }
}
