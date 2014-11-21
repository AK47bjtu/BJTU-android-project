package com.example.chinarecorder;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import com.example.util.PreferenceNameHelp;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	public static Activity mainActivity;
	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
//	private ListView mDrawerListView;
	public static TextView userName;
	public static boolean isLogined;
	
	private View mFragmentContainerView;
	
	private static String  recordFormat;
	
	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
	
	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = getActivity();
		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}
		
		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);
		getActivity().getFragmentManager().beginTransaction().replace(R.id.prefercontainer,
	                new MyPrefsFragment()).commit();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view= inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);
		userName = (TextView) view.findViewById(R.id.admintest);
		
//				this.getView().findViewById(R.id.leftlist);
//		mDrawerListView = (ListView) view.findViewById(R.id.leftlist);
//		mDrawerListView
//				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						selectItem(position);
//					}
//				});
//		mDrawerListView.setAdapter(new ArrayAdapter<String>(getActionBar()
//				.getThemedContext(), android.R.layout.simple_list_item_1,
//				android.R.id.text1, new String[] {
//						getString(R.string.title_section1),
//						getString(R.string.title_section2),
//						getString(R.string.title_section3), }));
//		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		return view;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.navigation_drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
		R.string.navigation_drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().supportInvalidateOptionsMenu(); // calls//.supportInvalidateOptionsMenu();
																// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
							.commit();
				}

				getActivity().supportInvalidateOptionsMenu(); // calls//.supportInvalidateOptionsMenu();
																// onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
//		if (mDrawerListView != null) {
//			mDrawerListView.setItemChecked(position, true);
//		}
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	/**
	 * 左侧边栏创建样式
	 * */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		if (mDrawerLayout != null && isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);//左侧边栏创建样式
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	/**
	 * action Bar 事件
	 * 
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {//action Bar 事件(次要)
		if (mDrawerToggle.onOptionsItemSelected(item)){//左上角bar
//			Toast.makeText(getActivity(), "left list", Toast.LENGTH_SHORT)
//			.show();
			return true;
		}
		
//		if (item.getItemId() == R.id.action_fileList) {//跳转录音文件列表按钮事件
//			Toast.makeText(getActivity(), "file List.", Toast.LENGTH_SHORT)
//					.show();
//			return true;
//		}

//		if (item.getItemId() == R.id.action_example) {
//			Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT)
//					.show();
//			return true;
//		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.    侧边栏标题显示
	 */
	private void showGlobalContextActionBar() {//侧边栏标题显示
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);//是否显示标题（是/否）
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setDisplayShowHomeEnabled(false);//是否显示标题图标(是/否)
//		actionBar.setDisplayUseLogoEnabled(false);//是否显示标题图标(是/否)<不知为啥无效>
		actionBar.setTitle(R.string.person);
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		
		void onNavigationDrawerItemSelected(int position);
	}
	
	 public static class MyPrefsFragment extends PreferenceFragment implements
	 	OnPreferenceChangeListener,OnPreferenceClickListener{

		//定义SharedPreferences对象  
		SharedPreferences sp;  
		//定义Preferences 文件中的键  
		
		 
		 ListPreference listPreferenceFormat;
		 PreferenceScreen preferenceScreenLogin;
		 
	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);

	            addPreferencesFromResource(R.xml.preferences);
	            
	            listPreferenceFormat = (ListPreference) findPreference("list_preference_format");
	            preferenceScreenLogin = (PreferenceScreen) findPreference("loginButton");
	            sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
	            recordFormat = sp.getString(PreferenceNameHelp.FOMAT_SETTING_KEY, null);
	            if (recordFormat == null) {
	            	recordFormat = listPreferenceFormat.getValue();
	            	saveOrUpdateForMat();
				}
	            listPreferenceFormat.setTitle("录音格式："+listPreferenceFormat.getValue());
	            
	            listPreferenceFormat.setOnPreferenceChangeListener(this);
	            listPreferenceFormat.setOnPreferenceClickListener(this);
	            preferenceScreenLogin.setOnPreferenceClickListener(this);
	            
	            
	        }

	        
			@Override
			public boolean onPreferenceClick(Preference preference) {
				if (preference == preferenceScreenLogin) {
//					Toast.makeText(getActivity(), "preferenceScreenLogin！", Toast.LENGTH_SHORT)
//					.show();
					if (!isLogined) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), LoginActivity.class);
						
						startActivityForResult(intent, 1);
					}else {
						isLogined = false;
						preferenceScreenLogin.setTitle(R.string.title_login_preferemce);
						preferenceScreenLogin.setSummary(R.string.summary_login_preference);
						userName.setText(R.string.unlogin);
					}

					return true;
				}
				return false;
			}


			@Override
			public boolean onPreferenceChange(Preference preference, Object objValue) {
				if (preference == listPreferenceFormat) {
					
					recordFormat = objValue.toString();
					listPreferenceFormat.setTitle("录音格式："+objValue.toString());
					saveOrUpdateForMat(); 
					return true;
				}else {
					return false;
				}
				
			}
			
			private void saveOrUpdateForMat(){
				sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
				SharedPreferences.Editor editor = sp.edit();  
		        //修改数据  
		        editor.putString(PreferenceNameHelp.FOMAT_SETTING_KEY, recordFormat);
		        editor.commit();
			}

			@Override
			public void onActivityResult(int requestCode, int resultCode,
					Intent data) {
				if(data!=null){
					isLogined = true;
				}else {
					isLogined = false;
				}
				if(isLogined){
					preferenceScreenLogin.setTitle("注销");
					preferenceScreenLogin.setSummary("点击以注销");
					userName.setText(data.getStringExtra("UserName"));
					saveOrUpdateLoginStatus(isLogined,data.getStringExtra("UserName"));
				}else {
					
				}
				
				super.onActivityResult(requestCode, resultCode, data);
			}
			
			private void saveOrUpdateLoginStatus(boolean loginStatus,String userName){
				sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
				SharedPreferences.Editor editor = sp.edit();  
		        //修改数据  
		        editor.putBoolean(PreferenceNameHelp.LOGIN_STATUS, loginStatus);
		        editor.putString(PreferenceNameHelp.USER_NAME, userName);
		        editor.commit();
			}
	        
	    }
	 
}
