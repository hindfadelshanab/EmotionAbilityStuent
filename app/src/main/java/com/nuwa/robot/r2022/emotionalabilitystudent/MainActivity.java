package com.nuwa.robot.r2022.emotionalabilitystudent;

import static com.nuwa.robot.r2022.emotionalabilitystudent.utils.Constants.PHASE_ANSWERED_FALSE;
import static com.nuwa.robot.r2022.emotionalabilitystudent.utils.Constants.PHASE_ANSWERED_TRUE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nuwa.robot.r2022.emotionalabilitystudent.adapter.ImageOptionAdapter;
import com.nuwa.robot.r2022.emotionalabilitystudent.databinding.ActivityMainBinding;
import com.nuwa.robot.r2022.emotionalabilitystudent.listener.OnImageOptionSelectedListener;
import com.nuwa.robot.r2022.emotionalabilitystudent.model.AnswerContent;
import com.nuwa.robot.r2022.emotionalabilitystudent.model.ImageOption;
import com.nuwa.robot.r2022.emotionalabilitystudent.model.Kind;
import com.nuwa.robot.r2022.emotionalabilitystudent.model.LetterCountLiveData;
import com.nuwa.robot.r2022.emotionalabilitystudent.model.Phase;
import com.nuwa.robot.r2022.emotionalabilitystudent.model.PhaseAnswered;
import com.nuwa.robot.r2022.emotionalabilitystudent.networking.StudentSocketClient;
import com.nuwa.robot.r2022.emotionalabilitystudent.utils.CheckImage;
import com.nuwa.robot.r2022.emotionalabilitystudent.utils.Constants;
import com.nuwa.robot.r2022.emotionalabilitystudent.utils.StateData;
import com.nuwa.robot.r2022.emotionalabilitystudent.viewModel.MainViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StudentSocketClient.IOnMessageListener, View.OnClickListener, View.OnDragListener {

    Gson gson;
    Phase phase;

    ActivityMainBinding binding;
    private StudentSocketClient studentClient;
    ImageOption selectedImageOption;
    private int count = 0;
    private MainViewModel mainViewModel;
    private boolean isWriting = false;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String serverIp = getIntent().getStringExtra("ip");
        gson = new Gson();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.txtA.setTag("A");
        binding.txtB.setTag("B");
        binding.txtC.setTag("C");
        binding.txtD.setTag("D");

        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishApp();

            }
        });
        initListener();


        if (serverIp != null) {
            try {
                Log.d("TAG", "RobotController: " + serverIp);
                studentClient = new StudentSocketClient(new URI("ws://" + serverIp + ":8887"));

            } catch (URISyntaxException e) {
                e.printStackTrace();
                Log.d("TAGee", "onCreate: 1 " + e.getMessage());

                Toast.makeText(this, "error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            try {
                studentClient.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("TAGee", "onCreate: 2 " + e.getMessage());
                Toast.makeText(this, "error :" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }


    }

    private void initListener() {

        binding.txtA.setOnClickListener(this);
        binding.txtB.setOnClickListener(this);
        binding.txtC.setOnClickListener(this);
        binding.txtD.setOnClickListener(this);
        binding.layoutLetterA.setOnDragListener(this);
        binding.layoutLetterB.setOnDragListener(this);
        binding.layoutLetterC.setOnDragListener(this);
        binding.layoutLetterD.setOnDragListener(this);
        binding.layoutLetterUnArrange.setOnDragListener(this);
        binding.layoutNext.setOnDragListener(this);

//
//        binding.itemPlaying1.setOnClickListener(this);
//        binding.itemPlaying2.setOnClickListener(this);
//        binding.itemWriting1.setOnClickListener(this);
//        binding.itemWriting2.setOnClickListener(this);
        binding.layoutItemsKindAccordingFunction.setOnDragListener(this);
        binding.writingLayout.setOnDragListener(this);
        binding.PlayingLayout.setOnDragListener(this);
    }

    @Override
    public void onMessage(String Message) {
        try {

            JSONObject jObj = new JSONObject(Message);
//            binding.layoutLetterUnArrange.setVisibility(View.GONE);
//            binding.layoutMainLetter.setVisibility(View.GONE);
//            binding.imageList.setVisibility(View.GONE);
//            binding.imageForShow.setVisibility(View.GONE);
//            binding.mainLayoutDrayByKindAccordingFunction.setVisibility(View.GONE);
//            binding.layoutItemsKindAccordingFunction.setVisibility(View.GONE);


            if (jObj.has(Constants.MESSAGE_FOR_STUDENT_KEY)) {
                String action = jObj.getString(Constants.MESSAGE_FOR_STUDENT_KEY);

                phase = gson.fromJson(action, Phase.class);
                Log.d("TAG", "onMessage: phase " + phase.toString());

                if (phase.getAnswerContent().getAnswerWay() == Constants.ANSWERED_WAY_DRAG &&
                        phase.getAnswerContent().getDragBy() == Constants.ANSWERED_DRAG_BY_LETTER) {
                    handleDragLetterContent(phase);
                    Log.d("TAG4", "onMessage: Show Drag letter  ");
                } else if (phase.getAnswerContent().getAnswerWay() == Constants.ANSWERED_WAY_DRAG &&
                        phase.getAnswerContent().getDragBy() == Constants.ANSWERED_DRAG_BY_KIND_ACCORDING_FUNCTION) {
                    handleDragAccordingFunction(phase);
                } else if (phase.getAnswerContent().getAnswerWay() == Constants.ANSWERED_WAY_DRAG &&
                        phase.getAnswerContent().getDragBy() == Constants.ANSWERED_DRAG_BY_WHAT_IS_NEXT) {
                    handleDragWhatIsNext();
                } else if (phase.getAnswerContent().getAnswerWay() == 2) {
                    Log.d("TAG", "OnMessageRecive: " + phase.getId());
                    Log.d("TAG", "OnMessageRecive: " + phase.getQuestioncontent().getTitle());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                        binding.layoutTxtSelectedAnswered.setVisibility(View.GONE);
//                        binding.imageSelectedAnswer.setVisibility(View.GONE);

                            binding.mainLayoutDrayByKindAccordingFunction.setVisibility(View.GONE);
                            binding.layoutItemsKindAccordingFunction.setVisibility(View.GONE);
                            binding.layoutImageForShow.setVisibility(View.GONE);
                            binding.imageSelectedAnswer.setVisibility(View.GONE);
                            binding.imageList.setVisibility(View.VISIBLE);
                            initAdapter(phase.getAnswerContent());

                        }
                    });
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAGee", "onMessage: 3 " + e.getMessage());
            Toast.makeText(this, "error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDragWhatIsNext() {

        binding.layoutLetterUnArrange.setVisibility(View.GONE);
        binding.layoutMainLetter.setVisibility(View.GONE);
        binding.imageList.setVisibility(View.GONE);

        binding.mainLayoutDrayByKindAccordingFunction.setVisibility(View.GONE);

        Log.d("TAG", "handleDragWhatIsNext: ");
        Log.d("TAG", "handleDragWhatIsNext: phase getDragBy" + phase.getAnswerContent().getDragBy());
        binding.layoutImageForShow.setVisibility(View.VISIBLE);
        binding.imageArrow.setVisibility(View.VISIBLE);
        binding.layoutNext.setVisibility(View.VISIBLE);
        binding.layoutNext.removeAllViews();
        binding.layoutItemsKindAccordingFunction.setVisibility(View.VISIBLE);

        binding.layoutItemsKindAccordingFunction.removeAllViews();

        if (phase.getAnswerContent().isHaveImageForShow()) {
            if (phase.getAnswerContent().getImageForShow().equals("vase")) {
                binding.imageForShow.setImageResource(R.drawable.vase);
            } else if (phase.getAnswerContent().getImageForShow().equals("egg")) {
                binding.imageForShow.setImageResource(R.drawable.egg2);

            } else if (phase.getAnswerContent().getImageForShow().equals("icCream")) {
                binding.imageForShow.setImageResource(R.drawable.ic_cram2);
            }
        }
//        binding.imageList.setVisibility(View.VISIBLE);
        List<ImageOption> imageOptions = phase.getAnswerContent().getImageOptions();


        for (int i = 0; i < imageOptions.size(); i++) {
            ImageView ii = new ImageView(this);
            ii.setMaxHeight(90);
            ii.setOnClickListener(this);
            ii.setMaxWidth(90);

            ii.setTag(String.valueOf(imageOptions.get(i).getIsCorrect()));
            switch (imageOptions.get(i).getImage()) {
                case "ic cream":
                    ii.setBackgroundResource(R.drawable.ic_cram);
                    break;
                case "egg":
                    ii.setBackgroundResource(R.drawable.egg);
                    break;
                case "Cactus":
                    ii.setBackgroundResource(R.drawable.cactus);
                    break;

            }


            binding.layoutItemsKindAccordingFunction.addView(ii);
        }
    }

    private void handleDragAccordingFunction(Phase phase) {

        binding.layoutLetterUnArrange.setVisibility(View.GONE);
        binding.layoutMainLetter.setVisibility(View.GONE);
        binding.imageList.setVisibility(View.GONE);
        binding.layoutImageForShow.setVisibility(View.GONE);
        binding.imageForShow.setVisibility(View.GONE);
        binding.mainLayoutDrayByKindAccordingFunction.setVisibility(View.VISIBLE);
        binding.layoutItemsKindAccordingFunction.setVisibility(View.VISIBLE);
        binding.PlayingLayout.removeAllViews();
        binding.writingLayout.removeAllViews();
        binding.layoutItemsKindAccordingFunction.removeAllViews();

        if (phase.getAnswerContent().getKind() != null) {
            List<Kind> kinds = phase.getAnswerContent().getKind();
            binding.txtFunction1.setText(kinds.get(0).getKindTitle());
            binding.txtFunction2.setText(kinds.get(1).getKindTitle());


        }
        if (phase.getAnswerContent().getImageOptions() != null) {
            List<ImageOption> imageOptions = phase.getAnswerContent().getImageOptions();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.layoutItemsKindAccordingFunction.removeAllViewsInLayout();

                }
            });
            for (int i = 0; i < 4; i++) {
                ImageView ii = new ImageView(this);
                ii.setMaxHeight(90);
                ii.setOnClickListener(this);
                ii.setMaxWidth(90);
                ii.setTag(imageOptions.get(i).getDescription());

                switch (imageOptions.get(i).getImage()) {

                    case "playing1":
                        ii.setBackgroundResource(R.drawable.playing1);
                        break;
                    case "playing2":
                        ii.setBackgroundResource(R.drawable.playing2);
                        break;
                    case "writing1":
                        ii.setBackgroundResource(R.drawable.writing1);
                        break;
                    case "writing2":
                        ii.setBackgroundResource(R.drawable.writing2);
                        break;
                    case "eating1":
                        ii.setBackgroundResource(R.drawable.eating1);
                        break;
                    case "eating2":
                        ii.setBackgroundResource(R.drawable.eating2);
                        break;
                    case "cleaning1":
                        ii.setBackgroundResource(R.drawable.cleaning1);
                        break;
                    case "cleaning2":
                        ii.setBackgroundResource(R.drawable.cleaning2);
                        break;
                    case "reading1":
                        ii.setBackgroundResource(R.drawable.reading1);
                        break;
                    case "reading2":
                        ii.setBackgroundResource(R.drawable.reading2);
                        break;
                    case "listening1":
                        ii.setBackgroundResource(R.drawable.listening1);
                        break;
                    case "listening2":
                        ii.setBackgroundResource(R.drawable.listening2);
                        break;
                    case "sleeping1":
                        ii.setBackgroundResource(R.drawable.sleeping1);
                        break;
                    case "sleeping2":
                        ii.setBackgroundResource(R.drawable.sleeping2);
                        break;
                    case "driving1":
                        ii.setBackgroundResource(R.drawable.driving1);
                        break;
                    case "driving2":
                        ii.setBackgroundResource(R.drawable.driving2);
                        break;

                    case "cooking1":
                        ii.setBackgroundResource(R.drawable.cooking1);
                        break;
                    case "cooking2":
                        ii.setBackgroundResource(R.drawable.cooking2);
                        break;
                    case "wearing1":
                        ii.setBackgroundResource(R.drawable.wearing1);
                        break;
                    case "wearing2":
                        ii.setBackgroundResource(R.drawable.wearing2);
                        break;

                    case "eating11":
                        ii.setBackgroundResource(R.drawable.eating11);
                        break;
                    case "eating12":
                        ii.setBackgroundResource(R.drawable.eating12);
                        break;
                    case "showering1":
                        ii.setBackgroundResource(R.drawable.showering1);
                        break;
                    case "showering2":
                        ii.setBackgroundResource(R.drawable.showering2);
                        break;
                }


                binding.layoutItemsKindAccordingFunction.addView(ii);
            }

        }

    }


    private void handleDragLetterContent(Phase phase) {


        binding.layoutLetterUnArrange.setVisibility(View.VISIBLE);
        binding.layoutMainLetter.setVisibility(View.VISIBLE);
        binding.imageList.setVisibility(View.GONE);
        binding.layoutImageForShow.setVisibility(View.GONE);
        binding.imageForShow.setVisibility(View.GONE);


    }


    public void initAdapter(AnswerContent answerContent) {

        List<ImageOption> imageOptionList = answerContent.getImageOptions();

        if (answerContent.isHaveImageForShow() && answerContent.getImageForShow().equals("ScaredChild")) {
            binding.layoutImageForShow.setVisibility(View.VISIBLE);
            binding.imageForShow.setVisibility(View.VISIBLE);
        } else if (answerContent.isHaveImageForShow() && answerContent.getImageForShow().equals("rectangle")) {
            binding.layoutImageForShow.setVisibility(View.VISIBLE);
            binding.imageForShow.setVisibility(View.VISIBLE);
            binding.imageForShow.setImageResource(R.drawable.orange_rectangle);
        } else if (answerContent.isHaveImageForShow() && answerContent.getImageForShow().equals("circle")) {
            binding.layoutImageForShow.setVisibility(View.VISIBLE);
            binding.imageForShow.setVisibility(View.VISIBLE);
            binding.imageForShow.setImageResource(R.drawable.large_circle);
        } else {
            binding.layoutImageForShow.setVisibility(View.GONE);
            binding.imageForShow.setVisibility(View.GONE);
        }
        ImageOptionAdapter adapter = new ImageOptionAdapter(this, imageOptionList, new OnImageOptionSelectedListener() {
            @Override
            public void OnImageOptionSelected(ImageOption imageOption) {

;
                if (answerContent.getAnswerWay() == Constants.ANSWERED_WAY_DRAG) {
                    selectedImageOption = imageOption;

                } else {
                    showSelectedImage(imageOption);
                    selectedImageOption = imageOption;

                    PhaseAnswered phaseAnswered = new PhaseAnswered();
                    phaseAnswered.setPhaseId(phase.getId());
                    phaseAnswered.setLevelId(phase.getLevelId());
                    phaseAnswered.setUnitId(phase.getUnitId());

                    if (imageOption.getIsCorrect() == 1) {
                        phaseAnswered.setAnswered(PHASE_ANSWERED_TRUE);
                        String phaseAnsweredJson = gson.toJson(phaseAnswered);
                        sendMessageForTeacher(phaseAnsweredJson);
                        Log.d("TAG", "OnImageOptionSelected: correct " + phaseAnsweredJson);

                    } else if (imageOption.getIsCorrect() == 0) {
                        phaseAnswered.setAnswered(PHASE_ANSWERED_FALSE);
                        String phaseAnsweredJson = gson.toJson(phaseAnswered);
                        Log.d("TAG", "OnImageOptionSelected: wrong " + phaseAnsweredJson);
                        sendMessageForTeacher(phaseAnsweredJson);

                    }
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.imageList.setLayoutManager(layoutManager);
        binding.imageList.setAdapter(adapter);
        if (answerContent.getImageForShow().equals("ScaredChild")) {
            binding.layoutImageForShow.setVisibility(View.VISIBLE);
            binding.imageForShow.setVisibility(View.VISIBLE);
        }
    }

    private void showSelectedImage(ImageOption imageOption) {

        binding.imageList.setVisibility(View.GONE);
        binding.layoutImageForShow.setVisibility(View.GONE);
        binding.imageForShow.setVisibility(View.GONE);
        binding.layoutImageForShow.setVisibility(View.GONE);
        binding.imageSelectedAnswer.setVisibility(View.VISIBLE);


        switch (imageOption.getImage()) {
            case "AngryFace":
                binding.imageSelectedAnswer.setImageResource(R.drawable.angry_emotion);
                break;
            case "SadFace":
                binding.imageSelectedAnswer.setImageResource(R.drawable.sad_emotion);
                break;
            case "SadBoy":
                binding.imageSelectedAnswer.setImageResource(R.drawable.sad_boy);
                break;
            case "AngryBoy":
                binding.imageSelectedAnswer.setImageResource(R.drawable.angry_boy);
                break;
            case "ScaredBoy":
                binding.imageSelectedAnswer.setImageResource(R.drawable.sacared_boy);
                break;
            case "HappyBoy":
                binding.imageSelectedAnswer.setImageResource(R.drawable.happy_boy);
                break;
            case "basketball":
                binding.imageSelectedAnswer.setImageResource(R.drawable.basketball);
                break;
            case "football":
                binding.imageSelectedAnswer.setImageResource(R.drawable.football);
                break;
            case "redBalloon":
                binding.imageSelectedAnswer.setImageResource(R.drawable.red_ballon);
                break;
            case "blueBalloon":
                binding.imageSelectedAnswer.setImageResource(R.drawable.blue_ballon);
                break;
            case "purpleIceCream":
                binding.imageSelectedAnswer.setImageResource(R.drawable.purple_ice_cream);
                break;
            case "blueIceCream":
                binding.imageSelectedAnswer.setImageResource(R.drawable.blue_ice_cream);
                break;

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                binding.imageSelectedAnswer.setVisibility(View.GONE);
            }
        }, 5000);
    }

    public void sendMessageForTeacher(String message) {
        if (studentClient.isOpen()) {
            message = "{\"phaseAnswered\":" + message + "}";
            studentClient.send(message);
            Log.d("TAG", "sendMessageForTeacher: 77" + message);
        }
    }

    public void sendCloseMessage(String message) {
        if (studentClient.isOpen()) {
            message = "{\"close\":" + message + "}";
            studentClient.send(message);
            Log.d("TAG", "sendMessageForTeacher: 77" + message);
        }
    }


    @Override
    public void onMessage(ByteBuffer message) {

    }

    @Override
    public void onError(Exception ex) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        StudentSocketClient.addIOnMessageListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StudentSocketClient.removeIOnMessageListener(this);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        // Handles each of the expected events
        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                    return true;
                }

                return false;

            case DragEvent.ACTION_DRAG_ENTERED:

                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
//                v.getBackground().clearColorFilter();
//                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:


                ClipData.Item item = event.getClipData().getItemAt(0);
                // Gets the text data from the item.
                String dragData = item.getText().toString();
                // Displays a message containing the dragged data.
                Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();
                // Turns off any color tints
//                v.getBackground().clearColorFilter();

                // Invalidates the view to force a redraw
//                v.invalidate();

                View vw = (View) event.getLocalState();

                ViewGroup owner = (ViewGroup) vw.getParent();
                owner.removeView(vw); //remove the dragged view

                //caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                LinearLayout container = (LinearLayout) v;
                if (phase.getAnswerContent().getAnswerWay() == Constants.ANSWERED_DRAG_BY_LETTER) {
                    container.removeAllViews();

                }

                container.addView(vw);//Add the dragged view
                container.bringChildToFront(vw);
                vw.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE
                Log.d("TAG", "onDrag: " + vw.getId());

                PhaseAnswered phaseAnswered = new PhaseAnswered();
                phaseAnswered.setPhaseId(phase.getId());
                phaseAnswered.setLevelId(phase.getLevelId());
                phaseAnswered.setUnitId(phase.getUnitId());

                if (container.getId() == R.id.layout_letter_A) {
                    TextView txtA = container.findViewById(R.id.txtA);

                    if (txtA != null) {
                        count += 1;
                        Log.d("TAG", "A Done: ");
                        Toast.makeText(this, "A Done", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("TAG", "A not Done: ");

                    }
                }
                if (container.getId() == R.id.layout_letter_B) {
                    TextView txtB = container.findViewById(R.id.txtB);

                    if (txtB != null) {
                        count += 1;
                        Toast.makeText(this, "B Done", Toast.LENGTH_SHORT).show();
                    }
                }
                if (container.getId() == R.id.layout_letter_C) {
                    TextView txtC = container.findViewById(R.id.txtC);

                    if (txtC != null) {
                        count += 1;
                        Toast.makeText(this, "C Done", Toast.LENGTH_SHORT).show();
                    }
                }
                if (container.getId() == R.id.layout_letter_D) {
                    TextView txtD = container.findViewById(R.id.txtD);

                    if (txtD != null) {
                        count += 1;
                        Toast.makeText(this, "D Done", Toast.LENGTH_SHORT).show();
                    }
                }


                if (count > 0) {
                    if (count == 4) {
                        phaseAnswered.setAnswered(PHASE_ANSWERED_TRUE);
                        String phaseAnsweredJson = gson.toJson(phaseAnswered);
                        Log.d("TAG", "handleDragLetterContent: phaseAnsweredJson " + phaseAnsweredJson);
                        sendMessageForTeacher(phaseAnsweredJson);
                    } else {
                        phaseAnswered.setAnswered(PHASE_ANSWERED_FALSE);
                        String phaseAnsweredJson = gson.toJson(phaseAnswered);
                        Log.d("TAG", "handleDragLetterContent: phaseAnsweredJson " + phaseAnsweredJson);
                        sendMessageForTeacher(phaseAnsweredJson);
                    }

                }


                Toast.makeText(this, "Count :" + count, Toast.LENGTH_SHORT).show();

                Log.d("TAG", "onDrag: count" + count);

                // Drag Kind According Function

                if (container.getId() == R.id.writingLayout) {
                    Log.d("TAG", "onDrag: writingLayout container " + container.getChildCount());

                    if (container.getChildCount() == 2) {

                        String kindType1 = phase.getAnswerContent().getKind().get(0).getKindTitle();
                        Log.d("TAG", "onDrag: writingLayout kindType1 " + kindType1);

                        if (container.getChildAt(0).getTag().equals(kindType1) &&
                                container.getChildAt(1).getTag().equals(kindType1)) {
                            isWriting = true;
                            Log.d("TAG", "onDrag: writingLayout isWriting " + isWriting);
                            Log.d("TAG", "onDrag: writingLayout container " + container.getChildCount());


                            Toast.makeText(this, "Writing Done", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                if (container.getId() == R.id.PlayingLayout) {
                    Log.d("TAG", "onDrag: PlayingLayout container " + container.getChildCount());

                    if (container.getChildCount() == 2) {
                        String kindType2 = phase.getAnswerContent().getKind().get(1).getKindTitle();
                        Log.d("TAG", "onDrag: PlayingLayout kindType2 " + kindType2);

                        if (container.getChildAt(0).getTag().equals(kindType2) &&
                                container.getChildAt(1).getTag().equals(kindType2)) {
                            isPlaying = true;
                            Log.d("TAG", "onDrag: PlayingLayout isWriting " + isPlaying);
                            Log.d("TAG", "onDrag: PlayingLayout container " + container.getChildCount());

                            Toast.makeText(this, "Writing Done", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if (owner.getId() == R.id.layoutItemsKindAccordingFunction) {
                    int i = owner.getChildCount();
                    if (i == 0) {
                        if (isWriting && isPlaying) {
                            // clientThread.sendMessageJson(Constants.KEY, Constants.CORRECT_MESSAGE);
                            Toast.makeText(this, "isWriting isPlaying", Toast.LENGTH_SHORT).show();

                            phaseAnswered.setAnswered(PHASE_ANSWERED_TRUE);
                            String phaseAnsweredJson = gson.toJson(phaseAnswered);
                            Log.d("TAG", ": phaseAnsweredJson  isWriting isPlaying" + phaseAnsweredJson);
                            sendMessageForTeacher(phaseAnsweredJson);


                            isWriting = false;
                            isPlaying = false;

                        } else {
                            phaseAnswered.setAnswered(PHASE_ANSWERED_FALSE);
                            String phaseAnsweredJson = gson.toJson(phaseAnswered);
                            Log.d("TAG", "handleDragLetterContent: phaseAnsweredJson  isWriting isPlaying" + phaseAnsweredJson);
                            sendMessageForTeacher(phaseAnsweredJson);

                            //  clientThread.sendMessageJson(Constants.KEY, Constants.WRONG_MESSAGE);
                            Toast.makeText(this, "failed i" + i, Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                if (phase.getAnswerContent().getDragBy() == Constants.ANSWERED_DRAG_BY_WHAT_IS_NEXT) {
                    if (container.getId() == R.id.layoutNext) {

                        if (container.getChildCount() == 1) {
                            if (container.getChildAt(0).getTag().equals("1")) {
                                phaseAnswered.setAnswered(PHASE_ANSWERED_TRUE);
                                String phaseAnsweredJson = gson.toJson(phaseAnswered);
                                Log.d("TAG", ": ANSWERED_DRAG_BY_WHAT_IS_NEXT phaseAnsweredJson " + phaseAnsweredJson);
                                sendMessageForTeacher(phaseAnsweredJson);
                            } else if (container.getChildAt(0).getTag().equals("0")) {
                                phaseAnswered.setAnswered(PHASE_ANSWERED_FALSE);
                                String phaseAnsweredJson = gson.toJson(phaseAnswered);
                                Log.d("TAG", "ANSWERED_DRAG_BY_WHAT_IS_NEXT: phaseAnsweredJson " + phaseAnsweredJson);
                                sendMessageForTeacher(phaseAnsweredJson);
                            }
                        }

                    }
                }

                return true;

            case DragEvent.ACTION_DRAG_ENDED:

                if (event.getResult()) {
                    LetterCountLiveData.get().postSuccess(count);

                    //         Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                // returns true; the value is ignored.

                return true;
            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(view);
        // Starts the drag
        view.startDrag(data        // data to be dragged
                , dragshadow   // drag shadow builder
                , view           // local data about the drag and drop operation
                , 0          // flags (not currently used, set to 0)
        );

    }

    private void finishApp() {

        try {
            sendCloseMessage("Student");
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
//            clientThread.sendMessageJson(Constants.KEY, Constants.STOP_MESSAGE);
//            finish();
//            System.exit(0);

        } catch (Exception e) {
            Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}