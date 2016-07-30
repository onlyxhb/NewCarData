package com.xhb.onlystar.newcardata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import com.pgyersdk.crash.PgyCrashManager;
import com.xhb.onlystar.apater.LeadAdapter;
import com.xhb.onlystar.bean.Rwzb;
import com.xhb.onlystar.utils.Constant;
import com.xhb.onlystar.utils.DataCleanManager;

import java.util.ArrayList;
import java.util.List;

public class RwzbTaskActivity extends BaseActivity {

    private ListView lead_task;
    private LeadAdapter adapter;
    private List<Rwzb> rwzb = new ArrayList<Rwzb>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rwzb_task);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        lead_task = (ListView) findViewById(R.id.lead_task);
        rwzb = Constant.RWZB_RESULT.getResult();
        adapter = new LeadAdapter(getApplicationContext(), rwzb);
        lead_task.setAdapter(adapter);
        PgyCrashManager.register(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RwzbTaskActivity.this, MainActivity_hq.class));
        DataCleanManager.clearAllCache(this);
        finish();
    }
}
