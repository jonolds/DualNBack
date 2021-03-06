package sapphire.dualnback;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Objects;

public class TabScoresFragment extends Fragment {
	public static ArrayList<String[]> sList;
	public static ScoreListAdapter scoreAdapt;
	String[] projection = {
			DualProvider.COL_DATE_TIME,
			DualProvider.COL_SCORE,
			DualProvider.COL_LEVEL};

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_scores_fragment, container, false);
		//get date, score and level from database in String form, add each to
		sList = new ArrayList<>();
		Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(DualProvider.CONTENT_URI,projection,null,null,"_ID DESC");
		if(Objects.requireNonNull(cursor).getCount() > 0) {
			cursor.moveToFirst();
			for(int i = 0; i < cursor.getCount(); i++) {
				String[] scoreElement = new String[3];
				scoreElement[0] = cursor.getString(0);

				scoreElement[1] = String.valueOf(cursor.getInt(1));
				scoreElement[2] = String.valueOf(cursor.getInt(2));
				sList.add(scoreElement);
				cursor.moveToNext();
			}
		}

		Button resetButton = view.findViewById(R.id.resetScoreBut);
		View.OnClickListener ocl = v -> {
			String[] projection = {DualProvider.COL_ID};
			Log.e("TabScorFrag", "listener");
			Cursor cursor1 = Objects.requireNonNull(getContext()).getContentResolver().query(DualProvider.CONTENT_URI,projection,null,null,"_ID DESC");
			assert cursor1 != null;
			if(cursor1.getCount() > 0) {
				cursor1.moveToFirst();
				for(int i = 0; i < cursor1.getCount(); i++){
					getContext().getContentResolver().delete(Uri.parse(DualProvider.CONTENT_URI + "/" + cursor1.getInt(0)),null,null);
					cursor1.moveToNext();
				}
				sList.clear();
				scoreAdapt.notifyDataSetChanged();
			}
		};
		resetButton.setOnClickListener(ocl);

		ListView listView = view.findViewById(R.id.list_view_scores);
		scoreAdapt = new ScoreListAdapter(getContext(), sList);
		listView.setAdapter(scoreAdapt);
		return view;
	}
//	public void tableData() {
//		Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(DualProvider.CONTENT_URI,projection,null,null,"_ID DESC");
//		if(cursor != null) {
//			if(cursor.getCount() > 0){
//				for(int i = 0; i < cursor.getCount(); i++) {
//					cursor.moveToPosition(i);
//					Log.e("ID ", String.valueOf(cursor.getInt(0)));
//					Log.e("Date/Time ", cursor.getString(1));
//					Log.e("Score ", String.valueOf(cursor.getInt(2)));
//					Log.e("Level ", String.valueOf(cursor.getInt(3)));
//				}
//			}
//			cursor.close();
//		}
//	}
}
