package com.example.nutritionQuery;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSONObject;
import com.example.common.SpacesItemDecoration;
import com.example.entity.ContentItem;
import com.example.R;
import com.example.entity.FoodItemClient;
import com.example.nutritionQuery.adapter.FoodsRecyclerAdapter;
import com.example.utils.HttpClientUtils;
import com.example.utils.InternetTooltip;
import com.example.utils.PicUtils;
import java.util.ArrayList;
import java.util.List;
public class NutritionFragment extends Fragment {
    private Spinner firstSpinner;
    private Spinner secondSpinner;
    private RecyclerView recyclerView;
    private List<String> firstTextViews = new ArrayList<>();
    private List<String> secondTextViews = new ArrayList<>();
    private List<ContentItem> contentItems = new ArrayList<>();
    private String firstValue = "";
    private String secondValue = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nutrition_query, container, false);
        firstSpinner = view.findViewById(R.id.nutrition_query_first_spinner);
        secondSpinner = view.findViewById(R.id.nutrition_query_second_spinner);
        recyclerView = view.findViewById(R.id.nutrition_query_content);
        initSpinner();
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void initSpinner() {
        String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getAllFirstKindFood", "");
        if (result == null) {
            InternetTooltip.tip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
            return;
        }
        firstTextViews = JSONObject.parseArray(result, String.class);
        List<TextView> firstText = new ArrayList<>();
        for (String value : firstTextViews) {
            TextView textView = new TextView(getActivity());
            textView.setText(value);
            textView.setGravity(Gravity.CENTER);
            firstText.add(textView);
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this.getActivity(), R.layout.nutrition_query_menu_item, R.id.nutrition_query_menu_text, firstTextViews);
        firstSpinner.setAdapter(adapter1);
        firstSpinner.setDropDownHorizontalOffset(1000);
        firstSpinner.setGravity(Gravity.CENTER);
        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstValue = (String) firstSpinner.getSelectedItem();
                secondTextViews.clear();
                String param = "firstKindFood=" + firstValue;
                String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getSecondKindByFirstKind", param);
                if (result == null) {
                    InternetTooltip.tip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
                    return;
                }
                secondTextViews = JSONObject.parseArray(result, String.class);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), R.layout.nutrition_query_menu_item, R.id.nutrition_query_menu_text, secondTextViews);
                secondSpinner.setAdapter(adapter2);
                secondSpinner.setGravity(Gravity.CENTER);
                secondSpinner.setDropDownHorizontalOffset(1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secondValue = (String) secondSpinner.getSelectedItem();
                String param = "secondKind=" + secondValue;
                String result = HttpClientUtils.httpPostRequest(HttpClientUtils.serverUrl + "getFoodsBySecondKind", param);
                if (result == null) {
                    InternetTooltip.tip(getActivity(), HttpClientUtils.HTTP_REQUEST_ERROR);
                    return;
                }
                List<FoodItemClient> clients = JSONObject.parseArray(result, FoodItemClient.class);
                contentItems.clear();
                for (FoodItemClient client : clients) {
                    contentItems.add(new ContentItem(client.getFoodName(), PicUtils.byteConvertToBitmap(client.getPicPath())));
                }
                showRecyclerView(contentItems);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void showRecyclerView(List<ContentItem> items) {
        FoodsRecyclerAdapter viewAdapter = new FoodsRecyclerAdapter(this.getActivity(), items);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.VERTICAL, false));
    }
}
