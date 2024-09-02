package com.svr.atown;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.svr.atown.adapter.ProductSilderAdapter;
import com.svr.atown.db.DBHelper;
import com.svr.atown.db.Tables;
import com.svr.atown.firebase.FirebaseData;
import com.svr.atown.holder.ProductHolder;
import com.svr.atown.listener.RecyclerTouchListener;
import com.svr.atown.pojo.OrderedProducts;
import com.svr.atown.pojo.UploadProduct;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean flag=false;
    Fragment fragments;
    RecyclerView productRecyclerView;
    DatabaseReference productReference;
    UploadProduct uploadProduct;
    TextView userTitle,userMail;
    String userStatus;
    NavigationView navigationView;
    ArrayList<UploadProduct> productList;
    FirebaseAuth firebaseAuth;
    DBHelper dbHelper;
    ViewPager viewPager;
    TabLayout tabLayout;
    List<String> backgroud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Thread.setDefaultUncaughtExceptionHandler(new CatchException());

        dbHelper=new DBHelper(getApplicationContext());

        productList=new ArrayList<>();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        productReference= FirebaseData.getProductReference();
        productRecyclerView=findViewById(R.id.productRecyclerView);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        navigationView=findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        //headerview content
        userTitle=headerView.findViewById(R.id.user);
        userMail=headerView.findViewById(R.id.user_mail_id);
        viewPager=findViewById(R.id.mainProductViewPager);
        tabLayout=findViewById(R.id.mainProductTabLayout);
        firebaseAuth=FirebaseData.getFirebaseAuth();
        backgroud=new ArrayList<>();

        Cursor cursor=dbHelper.getTableContent(Tables.user.tableName);
        Log.i("Size : ",String.valueOf(cursor.getCount()));
        cursor.moveToFirst();

        userTitle.setText(cursor.getString(cursor.getColumnIndex(Tables.user.name)));
        userMail.setText(cursor.getString(cursor.getColumnIndex(Tables.user.mail)));



        FirebaseData.getAdvertisementReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                backgroud.clear();
                Iterator it=snapshot.getChildren().iterator();
                while (it.hasNext()) {
                    DataSnapshot ds=(DataSnapshot) it.next();
                    Log.i("Image URI : ",ds.getValue().toString());
                    backgroud.add(ds.getValue().toString());
                }

                Log.i("Background Size : ",String.valueOf(backgroud.size()));

//                Log.i("Backgroud : ",backgroud.get(0));
                viewPager.setAdapter(new ProductSilderAdapter(MainActivity.this,backgroud));
                tabLayout.setupWithViewPager(viewPager,true);
//                Timer timer=new Timer();
//                timer.scheduleAtFixedRate(new SliderTimer(),3000,6000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        Cursor cursor=dbHelper.getTableContent(Tables.user.tableName);
//        cursor.moveToFirst();

        //String user=firebaseAuth.getCurrentUser().getEmail();

//        List<LoggedUser> loggedUserList=LoggedUser.find(LoggedUser.class,"mailid=?",user);
//
//        LoggedUser loggedUser=loggedUserList.get(0);
//
//        userMail.setText(loggedUser.getMail_id());
//        userTitle.setText(loggedUser.getUsername());
        userStatus=cursor.getString(cursor.getColumnIndex(Tables.user.status));
        Log.i("USER STATUS",userStatus);
        switch (userStatus) {
            case "user":
                hideMenuItem(R.id.upload);
                hideMenuItem(R.id.updateOffer);
                break;
            case "admin":
                hideMenuItem(R.id.displayOrder);
                hideMenuItem(R.id.trackOrder);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseRecyclerOptions<UploadProduct> firebaseRecyclerOptions=new FirebaseRecyclerOptions.
                Builder<UploadProduct>()
                .setQuery(productReference,UploadProduct.class)
                .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UploadProduct,ProductHolder>(firebaseRecyclerOptions) {


            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.product_row, parent, false);
                return new ProductHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull UploadProduct model) {
                holder.setProductTitle(model.getTitle());
                holder.setProductImage(model.getFirstImage(),MainActivity.this);
                int productsActualPrice=Integer.valueOf(model.getPrice());
                float discountOnProduct=Integer.valueOf(model.getDiscount());
                float percentValue=(discountOnProduct/100)*productsActualPrice;
                float price=productsActualPrice-percentValue;
                holder.setActualPrice(String.valueOf(Float.valueOf(model.getPrice())));
                holder.setProductPrice(String.valueOf(price));
                holder.setProductDiscount(model.getDiscount());
                productList.add(model);

            }
        };

        productRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

        if(userStatus.equals("user")) {
            productRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                    productRecyclerView,
                    new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            uploadProduct=productList.get(position);
                            if(uploadProduct!=null) {
                                Intent intent=new Intent(MainActivity.this,DisplayProduct.class);
                                Bundle bundle=new Bundle();
//                                Fragment fragment=new ShowProduct();
                                bundle.putSerializable("uploadProduct",uploadProduct);
                                intent.putExtra("uploadProductBundle",bundle);
                                startActivity(intent);
//                                fragment.setArguments(bundle);
//                                openFragment(fragment);
//                                hideFronView();
                            }
                        }

                        @Override
                        public void onHold(View view, int position) {
                            Toast.makeText(MainActivity.this,"Added To Cart",Toast.LENGTH_LONG).show();
                        }
                    }));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(flag) {
                super.onBackPressed();
            } else {
                Toast.makeText(getApplicationContext(),"Press Again To Exit...",Toast.LENGTH_LONG).show();
                flag=true;
            }
        }
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product, menu);
        switch (userStatus) {
            case "admin":
                hideMenuContent(R.id.cart,menu);
                return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//         Handle action bar item clicks here. The action bar will
//         automatically handle clicks on the Home/Up button, so long
//         as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        noinspection SimplifiableIfStatement
        if (id == R.id.cart) {
//            List orderedProducts= OrderedProducts.listAll(OrderedProducts.class);
            ArrayList<OrderedProducts> orderedProductsList=dbHelper.getCartList();
            if(orderedProductsList.size()<=0) {
                Toast.makeText(getApplicationContext(),"Cart Is Empty",Toast.LENGTH_LONG).show();
            } else {

                //fragment.setArguments(bundle);
                setTitle("Cart");
                //hideFronView();

                startActivity(new Intent(MainActivity.this,Cart.class));
                //openFragment(fragment);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(MainActivity.this,UserProfile.class));
                break;
            case R.id.placeOfferBanner:
                startActivity(new Intent(MainActivity.this,AdvertisementBanner.class));
                break;
            case R.id.upload:
                //setTitle("Upload Product");
                //fragments=new Upload();
                //setTitle("Upload Product");
                startActivity(new Intent(MainActivity.this,UploadItem.class));
                break;
            case R.id.updateOffer:
                startActivity(new Intent(MainActivity.this,UpdateItem.class));
//                fragments=new UpdateProduct();
//                setTitle("Update Details");
                break;
            case R.id.displayOrder:
                startActivity(new Intent(MainActivity.this,OrderHistory.class));
                break;
            case R.id.logout:
                FirebaseData.getFirebaseAuth().signOut();
                finish();
                startActivity(new Intent(getBaseContext(),Login.class));
                break;
            case R.id.aboutUS:
                Toast.makeText(getApplicationContext(),"About Us",Toast.LENGTH_LONG).show();
                break;
        }
        //openFragment(fragments);
        //hideFronView();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void openFragment(Fragment fragment) {
//        if(fragment!=null) {
//            getSupportFragmentManager().beginTransaction().add(R.id.replaceFragment,fragment).commit();
//        }
//    }

//    private void hideFronView() {
//        productRecyclerView.setVisibility(View.GONE);
//    }

    private void hideMenuItem(int id) {
        Menu menu = navigationView.getMenu();
        menu.findItem(id).setVisible(false);
    }

    private void hideMenuContent(int id,Menu menu) {
        MenuItem menuItem=menu.findItem(id);
        menuItem.setVisible(false);
    }

//    private class SliderTimer extends TimerTask {
//
//        @Override
//        public void run() {
////            if(activity!=null) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(viewPager.getCurrentItem()<(backgroud.size()-1)) {
//                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
//                    } else {
//                        viewPager.setCurrentItem(0);
//                    }
//                }
//            });
////            }
//        }
//    }


    public class CatchException implements Thread.UncaughtExceptionHandler{
        private String CRASH_NAME = "a_town_crash_report.txt";
        private String CRASH_PATH = Environment.getExternalStorageDirectory().getPath() + "/ECG_Crash_Report/";
        private Thread.UncaughtExceptionHandler defaultUEH;

        public CatchException() {
            this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            String timestamp = new SimpleDateFormat("ddMMyyHHmm").format(new Date());
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            String stacktrace = result.toString();
            printWriter.close();
            writeToFile(stacktrace, timestamp);
            defaultUEH.uncaughtException(t, e);
        }

        private void writeToFile(String stacktrace, String timeStamp) {
            try {
                String dir = CRASH_PATH;
                File file = new File(CRASH_PATH, timeStamp+"_"+CRASH_NAME);
                if (!file.exists()) {
                    //create folder
                    File folder = new File(dir); //folder name
                    if (!folder.isDirectory()) {
                        folder.mkdirs();
                    }
                    //create file
                    file.createNewFile();
                }
                BufferedWriter bos = new BufferedWriter(new FileWriter(
                        CRASH_PATH + "/" + timeStamp+"_"+CRASH_NAME));
                bos.write(stacktrace);
                bos.flush();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}