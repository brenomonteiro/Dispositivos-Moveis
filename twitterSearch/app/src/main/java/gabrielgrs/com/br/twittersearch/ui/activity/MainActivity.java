package gabrielgrs.com.br.twittersearch.ui.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import gabrielgrs.com.br.twittersearch.R;

public class MainActivity extends ListActivity {

    private ArrayAdapter<String> arrayAdapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> tagsList;
    private EditText queryEditText;
    private EditText tagEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initSharedPreferences();
        initListStrings();
        initAdapter();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (estaPreenchido()) {
                    addTaggedSearch(queryEditText.getText().toString(), tagEditText.getText().toString());
                    clearInputs();
                    Toast.makeText(MainActivity.this, "Query search salva com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tag = ((TextView) view).getText().toString();
                String urlString = getString(R.string.search_url) + Uri.encode(sharedPreferences.getString(tag, ""), "UTF-8");

                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));

                startActivity(webIntent);
            }
        });


        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String tag = ((TextView) view).getText().toString();

                AlertDialog.Builder builder = initDialogItemListClick(tag);
                builder.create().show();
                return true;
            }
        });
    }

    @NonNull
    private AlertDialog.Builder initDialogItemListClick(final String tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        initDialogLongItemClick(tag, builder);
        return builder;
    }

    private void initDialogLongItemClick(final String tag, AlertDialog.Builder builder) {
        builder.setTitle(getString(R.string.tag_dialog).replace("{tag}", tag));

        builder.setItems(R.array.dialog_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        shareSearch(tag);
                        break;
                    case 1:
                        tagEditText.setText(tag);
                        queryEditText.setText(sharedPreferences.getString(tag, ""));
                        break;
                    case 2:
                        deleteSearch(tag);
                        break;
                }
            }
        });

        builder.setNegativeButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }

    private void deleteSearch(final String tag) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);

        confirmBuilder.setMessage(getString(R.string.confirm_message, tag));

        confirmBuilder.setNegativeButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        confirmBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tagsList.remove(tag);

                SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                preferencesEditor.remove(tag);
                preferencesEditor.apply();

                arrayAdapter.notifyDataSetChanged();
            }
        });

        confirmBuilder.create().show();
    }

    private void shareSearch(String tag) {
        String urlString = getString(R.string.search_url) + Uri.encode(sharedPreferences.getString(tag, ""), "UTF-8");

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));

        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message, urlString));
        shareIntent.setType("text/plain");

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_search)));
    }

    private boolean estaPreenchido() {
        return queryEditText.getText().length() > 0 && tagEditText.getText().length() > 0;
    }

    private void clearInputs() {
        queryEditText.setText("");
        tagEditText.setText("");
    }

    private void addTaggedSearch(String query, String tag) {
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(tag, query);
        preferencesEditor.apply();

        if (!tagsList.contains(tag)) {
            tagsList.add(tag);
            Collections.sort(tagsList, String.CASE_INSENSITIVE_ORDER);
            arrayAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();

        }
    }

    private void initListStrings() {
        tagsList = new ArrayList<>(sharedPreferences.getAll().keySet());
        Collections.sort(tagsList, String.CASE_INSENSITIVE_ORDER);
    }

    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences("tags", MODE_PRIVATE);
    }

    private void initAdapter() {
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, tagsList);
        setListAdapter(arrayAdapter);
    }

    private void initViews() {
        queryEditText = findViewById(R.id.main_twitter_search_query_edittext);
        tagEditText = findViewById(R.id.main_tag_your_query_edittext);
        saveButton = findViewById(R.id.main_save_button);
    }
}
