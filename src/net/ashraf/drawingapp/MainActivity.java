package net.ashraf.drawingapp;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private DrawingView drawView;
	private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, loadImg;
	private float verySmallBrush, smallBrush, mediumBrush, largeBrush;
	private EditText hexNumber;
	Intent intent;
	Button testBox;
	int colorNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		init();

		setOnClickAction();

		drawView.setBrushSize(mediumBrush);
		testBox.setBackgroundColor(0xFF000000);

		intent = new Intent(this, Splash.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT", true);

	}

	public void paintClicked(View view) {
		drawView.setErase(false);
		drawView.setBrushSize(drawView.getLastBrushSize());
		if (view.getId() == R.id.customHex) {
			String color = hexNumber.getText().toString();
			if (checkInput(color)) {
				drawView.setColor(color);
				currPaint.setImageDrawable(getResources().getDrawable(
						R.drawable.paint));
				colorNumber = Color.parseColor(color);
				testBox.setBackgroundColor(colorNumber);
			} else {
				hexNumber.setError("Invalid Color");
			}
		} else if (view != currPaint) {
			hexNumber.setText("");
			ImageButton imgView = (ImageButton) view;
			String color = view.getTag().toString();
			colorNumber = Color.parseColor(color);
			testBox.setBackgroundColor(colorNumber);
			drawView.setColor(color);

			imgView.setImageDrawable(getResources().getDrawable(
					R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(
					R.drawable.paint));
			currPaint = (ImageButton) view;
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.draw_btn) {
			drawView.setErase(false);
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush size:");
			brushDialog.setContentView(R.layout.brush_chooser);

			ImageButton verySmallBtn = (ImageButton) brushDialog
					.findViewById(R.id.v_small_brush);
			verySmallBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(verySmallBrush);
					drawView.setLastBrushSize(verySmallBrush);
					brushDialog.dismiss();
				}
			});

			ImageButton smallBtn = (ImageButton) brushDialog
					.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(smallBrush);
					drawView.setLastBrushSize(smallBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton mediumBtn = (ImageButton) brushDialog
					.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(mediumBrush);
					drawView.setLastBrushSize(mediumBrush);
					brushDialog.dismiss();
				}
			});

			ImageButton largeBtn = (ImageButton) brushDialog
					.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(largeBrush);
					drawView.setLastBrushSize(largeBrush);
					brushDialog.dismiss();
				}
			});

			SeekBar seekBar = (SeekBar) brushDialog.findViewById(R.id.seekBar1);
			final TextView textView = (TextView) brushDialog
					.findViewById(R.id.count);
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				int progress = 0;

				@Override
				public void onProgressChanged(SeekBar seekBar,
						int progresValue, boolean fromUser) {
					progress = progresValue;
					textView.setText("Current value : " + progress);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					textView.setText("Current value : " + progress);
					drawView.setBrushSize(progress);
					drawView.setLastBrushSize(progress);
					brushDialog.dismiss();
				}
			});

			brushDialog.show();
		} else if (view.getId() == R.id.erase_btn) {
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Eraser size:");
			brushDialog.setContentView(R.layout.brush_chooser);

			ImageButton verySmallBtn = (ImageButton) brushDialog
					.findViewById(R.id.v_small_brush);
			verySmallBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(verySmallBrush);
					brushDialog.dismiss();
				}
			});

			ImageButton smallBtn = (ImageButton) brushDialog
					.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(smallBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton mediumBtn = (ImageButton) brushDialog
					.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(mediumBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton largeBtn = (ImageButton) brushDialog
					.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(largeBrush);
					brushDialog.dismiss();
				}
			});

			SeekBar seekBar = (SeekBar) brushDialog.findViewById(R.id.seekBar1);
			final TextView textView = (TextView) brushDialog
					.findViewById(R.id.count);
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				int progress = 0;

				@Override
				public void onProgressChanged(SeekBar seekBar,
						int progresValue, boolean fromUser) {
					progress = progresValue;
					textView.setText("Current value : " + progress);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					drawView.setErase(true);
					textView.setText("Current value : " + progress);
					drawView.setBrushSize(progress);
					drawView.setLastBrushSize(progress);
					brushDialog.dismiss();
				}
			});
			brushDialog.show();
		} else if (view.getId() == R.id.new_btn) {
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("New drawing");
			newDialog
					.setMessage("Start new drawing (you will lose the current drawing)?");
			newDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							drawView.startNew();
							drawView.setBackgroundColor(Color.WHITE);
							dialog.dismiss();
						}
					});
			newDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			newDialog.show();
		} else if (view.getId() == R.id.load_btn) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select background"), 1);
		} else if (view.getId() == R.id.save_btn) {
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing to device Gallery?");

			final EditText input = new EditText(MainActivity.this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			input.setHint("Enter drawing Name !");
			input.setLayoutParams(lp);
			saveDialog.setView(input);

			saveDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							drawView.setDrawingCacheEnabled(true);
							String imgName = input.getText().toString();
							if (imgName.matches(""))
								imgName = "default";
							String imgSaved = MediaStore.Images.Media
									.insertImage(getContentResolver(),
											drawView.getDrawingCache(), imgName
													+ ".png", "drawing");
							if (imgSaved != null) {
								Toast savedToast = Toast.makeText(
										getApplicationContext(),
										"Drawing saved to Gallery!",
										Toast.LENGTH_SHORT);
								savedToast.show();
							} else {
								Toast unsavedToast = Toast.makeText(
										getApplicationContext(),
										"Oops! Image could not be saved.",
										Toast.LENGTH_SHORT);
								unsavedToast.show();
							}
							drawView.destroyDrawingCache();

						}
					});
			saveDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			saveDialog.show();
		}
	}

	private void init() {
		drawView = (DrawingView) findViewById(R.id.drawing);
		LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);

		currPaint = (ImageButton) paintLayout.getChildAt(5);
		// currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);
		drawBtn = (ImageButton) findViewById(R.id.draw_btn);
		eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
		newBtn = (ImageButton) findViewById(R.id.new_btn);
		saveBtn = (ImageButton) findViewById(R.id.save_btn);
		loadImg = (ImageButton) findViewById(R.id.load_btn);
		hexNumber = (EditText) findViewById(R.id.edHexNumber);

		testBox = (Button) findViewById(R.id.testBox);
		
	}

	private void setOnClickAction() {
		drawBtn.setOnClickListener(this);

		eraseBtn.setOnClickListener(this);

		newBtn.setOnClickListener(this);

		saveBtn.setOnClickListener(this);

		loadImg.setOnClickListener(this);

	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Exit")
				.setMessage("Are you sure you want to exit?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								startActivity(intent);
							}
						}).setNegativeButton("No", null).show();
	}

	private boolean checkInput(String color) {
		String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
		Pattern pattern = Pattern.compile(HEX_PATTERN);
		Matcher matcher = pattern.matcher(color);
		return matcher.matches();
	}

	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				Uri selectedImageUri = data.getData();

				Drawable bg = null;
				try {
					InputStream inputStream = getContentResolver()
							.openInputStream(selectedImageUri);
					bg = Drawable.createFromStream(inputStream,
							selectedImageUri.toString());
				} catch (FileNotFoundException e) {
					// bg = ContextCompat.getDrawable(this, R.drawable.bg);
				}

				drawView.startNew();
				drawView.setBackground(bg);
			}
		}
	}
}
