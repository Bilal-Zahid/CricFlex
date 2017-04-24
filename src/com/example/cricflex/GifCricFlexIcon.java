package com.example.cricflex;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;


public class GifCricFlexIcon extends ProgressDialog {
  private AnimationDrawable animation;

  public static ProgressDialog ctor(Context context) {
    GifCricFlexIcon dialog = new GifCricFlexIcon(context);
    dialog.setIndeterminate(true);
    dialog.setCancelable(false);
    return dialog;
  }

  public GifCricFlexIcon(Context context) {
    super(context);
  }

  public GifCricFlexIcon(Context context, int theme) {
    super(context, theme);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.anim_custom_dialog);

    ImageView la = (ImageView) findViewById(R.id.animation);
    la.setBackgroundResource(R.drawable.gif_cricflex_icon);
    animation = (AnimationDrawable) la.getBackground();

//    ImageView la = (ImageView) findViewById(R.id.animation);
//    la.setImageResource(R.drawable.gif_cricflex_icon);
//    animation = (AnimationDrawable) la.getDrawable();
  }

  @Override
  public void show() {
    super.show();
    animation.start();
  }

  @Override
  public void dismiss() {
    super.dismiss();
    animation.stop();
  }
}