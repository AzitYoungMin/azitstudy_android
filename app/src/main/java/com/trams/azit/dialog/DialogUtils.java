package com.trams.azit.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.trams.azit.R;

/**
 * Created by sonnv on 1/13/2016.
 */
public class DialogUtils {

    public static void showChooseSecondLanguageDialog(final Context context, final SelectSecondLanguage secondLanguage) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_select_seconde_language);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        final ImageView iconClose = (ImageView) dialog.findViewById(R.id.iconClose);
        iconClose.setTag(-1);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                secondLanguage.onSelectSecondLanguage((int) v.getTag());
            }
        };


        Button btChinese = (Button) dialog.findViewById(R.id.btChinese);
        Button btJapanese = (Button) dialog.findViewById(R.id.btJapanese);
        Button btVietnamese = (Button) dialog.findViewById(R.id.btVietnamese);
        Button btArabic = (Button) dialog.findViewById(R.id.btArabic);
        Button btGerman = (Button) dialog.findViewById(R.id.btGerman);
        Button btSpanish = (Button) dialog.findViewById(R.id.btSpanish);
        Button btFrench = (Button) dialog.findViewById(R.id.btFrench);
        Button btRussian = (Button) dialog.findViewById(R.id.btRussian);
        Button btChineseCharacter = (Button) dialog.findViewById(R.id.btChineseCharacter);

        btChinese.setTag(0);
        btJapanese.setTag(1);
        btVietnamese.setTag(2);
        btArabic.setTag(3);
        btGerman.setTag(4);
        btSpanish.setTag(5);
        btFrench.setTag(6);
        btRussian.setTag(7);
        btChineseCharacter.setTag(8);

        iconClose.setOnClickListener(listener);
        btChinese.setOnClickListener(listener);
        btJapanese.setOnClickListener(listener);
        btVietnamese.setOnClickListener(listener);
        btArabic.setOnClickListener(listener);
        btGerman.setOnClickListener(listener);
        btSpanish.setOnClickListener(listener);
        btFrench.setOnClickListener(listener);
        btRussian.setOnClickListener(listener);
        btChineseCharacter.setOnClickListener(listener);

        if (dialog != null) {
            dialog.show();
        }

        dialog.getWindow().setAttributes(lp);
    }

    public interface SelectSecondLanguage {
        public void onSelectSecondLanguage(int languageIndex);
    }

    public interface ConfirmDialogListener {
        public void onConfirmClick();
    }

    public interface ConfirmDialogOkCancelListener {
        public void onOkClick();

        public void onCancelClick();
    }

    public static final AlertDialog showConfirmAlertDialog(final Context context, final String msg, final ConfirmDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);

        if (listener != null) {
            builder.setPositiveButton(context.getString(R.string.OK),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            listener.onConfirmClick();

                        }
                    });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static final AlertDialog showConfirmAndCancelAlertDialog(final Context context, final String msg, final ConfirmDialogOkCancelListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);

        if (listener != null) {
            builder.setPositiveButton(context.getString(R.string.ok_korea),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            listener.onOkClick();

                        }
                    });

            builder.setNegativeButton(context.getString(R.string.cancel_korea), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onCancelClick();
                }
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
}
