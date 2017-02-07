package com.example.guillaume.adict_impact;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static android.R.style.Animation;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.random;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.Animator;

/**
 * Created by Guillaume on 17/05/2016.
 */
public class HomeFragment extends Fragment {

    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> pairedDevices = null;
    String[] tabPaired;
    ArrayAdapter<String> adapter = null;
    boolean boulConnected;
    ObjetGlobalVar globalVar;
    Thread thread;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    volatile boolean stopWorker;
    byte[] readBuffer;
    int readBufferPosition;
    public ObjetFrappe punch = new ObjetFrappe(null, null, null, null, null, null, null, null, null);
    private static final int OK = 0x0001;
    private static final int NOK = 0x0002;
    Activity act;
    boolean running = true;

    ProgressBar force;
    ProgressBar force2;
    ProgressBar force3;

    ProgressBar vit;
    ProgressBar vit2;
    ProgressBar vit3;

    ProgressBar puiss;
    ProgressBar puiss2;
    ProgressBar puiss3;

    TextView forceVal;
    TextView force2Val;
    TextView force3Val;

    TextView vitVal;
    TextView vit2Val;
    TextView vit3Val;

    TextView puissVal;
    TextView puiss2Val;
    TextView puiss3Val;

    ImageView impact;
    ImageView impact2;
    ImageView impact3;

    DecimalFormat df;

    float precisionX=0;
    float precisionY=0;
    float precisionX2=0;
    float precisionY2=0;
    float precisionX3=0;
    float precisionY3=0;

    ImageView j1;
    ImageView j2;
    ImageView end;

    TextView j1_icon;
    TextView j2_icon;

    TextView win1;
    TextView win2;

    Button frapper = null;
    Button restart = null;

    float dest1 = 0, dest2=0;

    float rot = 0;

    float pos_start;

    float dist_remain1 = 0, dist_remain2 = 0;

    int i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // keep the fragment and all its data across screen rotation
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.onglet_home2, container, false);
        act=getActivity();

        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        force = (ProgressBar) view.findViewById(R.id.force);
        force2 = (ProgressBar) view.findViewById(R.id.force2);
        force3 = (ProgressBar) view.findViewById(R.id.force3);

        vit = (ProgressBar) view.findViewById(R.id.vit);
        vit2 = (ProgressBar) view.findViewById(R.id.vit2);
        vit3 = (ProgressBar) view.findViewById(R.id.vit3);

        puiss = (ProgressBar) view.findViewById(R.id.puiss);
        puiss2 = (ProgressBar) view.findViewById(R.id.puiss2);
        puiss3 = (ProgressBar) view.findViewById(R.id.puiss3);

        forceVal = (TextView) view.findViewById(R.id.forceVal);
        force2Val = (TextView) view.findViewById(R.id.force2Val);
        force3Val = (TextView) view.findViewById(R.id.force3Val);

        vitVal = (TextView) view.findViewById(R.id.vitVal);
        vit2Val = (TextView) view.findViewById(R.id.vit2Val);
        vit3Val = (TextView) view.findViewById(R.id.vit3Val);

        puissVal = (TextView) view.findViewById(R.id.puissVal);
        puiss2Val = (TextView) view.findViewById(R.id.puiss2Val);
        puiss3Val = (TextView) view.findViewById(R.id.puiss3Val);

        impact = (ImageView) view.findViewById(R.id.impact);
        impact2 = (ImageView) view.findViewById(R.id.impact2);
        impact3 = (ImageView) view.findViewById(R.id.impact3);
        //impact.setVisibility(View.INVISIBLE);
        //impact2.setVisibility(View.INVISIBLE);
        //impact3.setVisibility(View.INVISIBLE);

        j1 = (ImageView) view.findViewById(R.id.j1);
        j2 = (ImageView) view.findViewById(R.id.j2);
        end = (ImageView) view.findViewById(R.id.end);

        j1_icon = (TextView) view.findViewById(R.id.j1_icon);
        j2_icon = (TextView) view.findViewById(R.id.j2_icon);
        j1_icon.setVisibility(View.VISIBLE);
        j2_icon.setVisibility(View.INVISIBLE);

        win1 = (TextView) view.findViewById(R.id.win1);
        win2 = (TextView) view.findViewById(R.id.win2);
        win1.setVisibility(View.INVISIBLE);
        win2.setVisibility(View.INVISIBLE);

        frapper = (Button) view.findViewById(R.id.frapper_btn);
        frapper.setOnClickListener(frapperListener);

        restart = (Button) view.findViewById(R.id.restart_btn);
        restart.setOnClickListener(restartListener);

        pos_start = 806;

        int pos_end = end.getTop()+end.getHeight();

        int ok=1;

        connexion();
        /*i=1;
        connexion();
        i=2;
        connexion();*/

        return view;
    }

    // Uniquement pour le bouton "frapper"
    protected View.OnClickListener frapperListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            avanceAnimation();

        }
    };



    public void avanceAnimation() {
        if (j1_icon.getVisibility() == View.VISIBLE) {
            dest1 += 250*random();
            frapper.setOnClickListener(null);
            restart.setOnClickListener(null);
            final ViewPropertyAnimator animation1 = j1.animate().translationY(-dest1).setInterpolator(new LinearInterpolator()).setDuration(500);

            animation1.setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationEnd(final android.animation.Animator animation) {
                    j1_icon.setVisibility(View.INVISIBLE);
                    j2_icon.setVisibility(View.VISIBLE);
                    frapper.setOnClickListener(frapperListener);
                    restart.setOnClickListener(restartListener);
                    winAnimation();
                }

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }
        else {
            dest2 += 250*random();
            frapper.setOnClickListener(null);
            restart.setOnClickListener(null);
            final ViewPropertyAnimator animation2 = j2.animate().translationY(-dest2).setInterpolator(new LinearInterpolator()).setDuration(500);

            animation2.setListener(new Animator.AnimatorListener() {


                @Override
                public void onAnimationEnd(final android.animation.Animator animation) {
                    j1_icon.setVisibility(View.VISIBLE);
                    j2_icon.setVisibility(View.INVISIBLE);
                    frapper.setOnClickListener(frapperListener);
                    restart.setOnClickListener(restartListener);
                    winAnimation();
                }

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

    }

    public void winAnimation() {
        dist_remain1 = (j1.getTop() - (end.getTop() + end.getHeight())) - dest1;
        if (dist_remain1 <= -8) {
            win1.setVisibility(View.VISIBLE);
            frapper.setOnClickListener(null);
            restart.setOnClickListener(null);
            final ViewPropertyAnimator animation1 = win1.animate().rotation(rot).setInterpolator(new LinearInterpolator()).setDuration(3000);

            animation1.setListener(new Animator.AnimatorListener() {


                @Override
                public void onAnimationEnd(final android.animation.Animator animation) {
                    win1.setVisibility(View.INVISIBLE);
                    restart();

                }

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }

        dist_remain2 = (j2.getTop() - (end.getTop() + end.getHeight())) - dest2;
        if (dist_remain2 <= -8) {
            win2.setVisibility(View.VISIBLE);
            frapper.setOnClickListener(null);
            restart.setOnClickListener(null);
            final ViewPropertyAnimator animation1 = win2.animate().rotation(rot).setInterpolator(new LinearInterpolator()).setDuration(3000);

            animation1.setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationEnd(final android.animation.Animator animation) {
                    win2.setVisibility(View.INVISIBLE);
                    restart();
                }

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }


    };

    // Uniquement pour le bouton "restart"
    protected View.OnClickListener restartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            restart();
        }
    };

    public void restart() {
        j1.setY(pos_start);
        j2.setY(pos_start);

        dest1 = 0;
        dest2 = 0;


        j1_icon.setVisibility(View.VISIBLE);
        j2_icon.setVisibility(View.INVISIBLE);

        frapper.setOnClickListener(frapperListener);
        restart.setOnClickListener(restartListener);
        /*if ((win1.getVisibility() == View.VISIBLE) || (win2.getVisibility() == View.VISIBLE)) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            win1.setVisibility(View.INVISIBLE);
            win2.setVisibility(View.INVISIBLE);
        }*/

    }


    @Override
    public void onStop() {
        super.onStop();
        close();
    }

    @Override
    public void onPause() {
        super.onPause();
        close();
    }

    public void close() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void connexion() {
        Intent intentB = getActivity().getIntent();
        globalVar = (ObjetGlobalVar)intentB.getSerializableExtra("objVar");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
        }

        int indCheked = 0;
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        int gg = 0;
        if(pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mmDevice = device;
                if (gg == indCheked) {
                    break;
                }
                gg = gg + 1;
            }
            if (!boulConnected) {
                //lancement de la page de dialogue et de la connection au BT
                //mProgressDialog = ProgressDialog.show(this, "Patientez", "Connexion à " + mmDevice.getName() + " ...", true);
                //start a new thread to process job
                thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(200);
                            //connection au bluetooth
                            try {
                                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                                mmSocket.connect();
                                mmOutputStream = mmSocket.getOutputStream();
                                mmInputStream = mmSocket.getInputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (mmSocket.isConnected() == true) {
                                handler1.sendEmptyMessage(OK);
                                globalVar.setBoulConnected(true);


                            } else {
                                handler1.sendEmptyMessage(NOK);
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        }
    }
    void beginListenForData() {
        final int[] k = {0};
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[6144];
        workerThread = new Thread(new Runnable() {

            public void run() {
                byte[] buffer = new byte[1024];  // buffer store for the stream
                String message = "";
                // Keep listening to the InputStream until an exception occurs
                while (running) {
                    try {
                        message = "";
                        k[0] = mmInputStream.read(buffer, 0, 1024);
                        if (k[0] > 0) {
                            for (int i = 0; i < k[0]; i++) {
                                message = message + new String(new byte[]{buffer[i]});
                                buffer[i] = '\0';
                            }
                            final String finalMessage = message;

                            Thread toRun = new Thread() {
                                public void run() {
                                    Message msg = handler.obtainMessage();
                                    Bundle b = new Bundle();
                                    b.putString("receivedData", finalMessage);
                                    msg.setData(b);
                                    handler.sendMessage(msg);

                                }
                            };
                            toRun.start();
                        }
                    } catch (IOException e) {
                        break;
                    }
                }
            }
        });
        workerThread.start();
    }

    public String Data = "";
    public int coco = 0;
    private Double aa1 = 0.0000013, aa2 = 0.00000346, aa3 = 0.000002, bb = 0.00097752;// bb = 0.00102;
    boolean dejaAffiche = false;
    boolean dejaAffiche2 = false;
    boolean queDesChiffres = true;
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case OK:
                    break;
                case NOK:
                    punch = new ObjetFrappe(null, null, null, null, null, null, null, null, null);
                    break;
            }
        }
    };

    private void Simulation(Double[] forceCapteur_1, Double[] forceCapteur_2, Double[] forceCapteur_3) {
        Double dT = 6.4528e-04; ///////////////////// A vérifier
        int nbVal = forceCapteur_1.length;
        Double[] forceVecteurBrut = new Double[nbVal];
        Double[] forceVecteurFrappe = new Double[nbVal - 1];
        Double[] mv = new Double[nbVal];
        Double[] vitesse = new Double[nbVal];
        Double[] masse = new Double[nbVal];
        Double[] position = new Double[nbVal];
        Double[] temps = new Double[nbVal];
        Double[] fMaki_p = new Double[nbVal];
        Double[] fMaki_v = new Double[nbVal];
        Double[] fMaki = new Double[nbVal];
        Double[] positionPourcent = new Double[nbVal];
        Double m2 = 0.130;//tous = 301g, housse = 117g, clapet = 104g, mousse = 76g

        int indposMini = 0;

        Double[][] tabRaid = {{0.001, 0.002, 0.002, 0.003, 0.004, 0.005, 0.006, 0.006, 0.007, 0.008, 0.009, 0.010, 0.010, 0.011, 0.012, 0.013, 0.014, 0.015, 0.016, 0.017, 0.018, 0.019, 0.020, 0.021,
                0.022, 0.023, 0.024, 0.025, 0.026, 0.027, 0.028, 0.029, 0.030, 0.031, 0.032, 0.033, 0.034, 0.035, 0.036, 0.037, 0.038, 0.039, 0.040, 0.041, 0.042, 0.043, 0.044, 0.045, 0.046, 0.047,
                0.048, 0.049, 0.050, 0.051, 0.052, 0.053, 0.054, 0.055, 0.056, 0.057, 0.058, 0.059, 0.060, 0.061, 0.062, 0.063, 0.064, 0.065, 0.066, 0.067, 0.068, 0.069, 0.070, 0.071, 0.072, 0.073,
                0.074, 0.075, 0.076, 0.077, 0.078, 0.079, 0.080, 0.081, 0.082, 0.083, 0.084, 0.085, 0.086, 0.087, 0.088, 0.089, 0.090, 0.091, 0.092, 0.093, 0.094, 0.095, 0.096, 0.097, 0.098, 0.099,
                0.100, 0.101, 0.102, 0.103, 0.104, 0.105, 0.106, 0.107, 0.108, 0.109, 0.110, 0.111, 0.112, 0.113, 0.114, 0.115, 0.116, 0.117, 0.118, 0.119, 0.120, 0.121, 0.122, 0.123, 0.124, 0.125,
                0.126, 0.127, 0.128, 0.129, 0.130, 0.131, 0.132, 0.133, 0.134, 0.135, 0.136, 0.137, 0.138, 0.139, 0.140, 0.141, 0.142, 0.143, 0.144, 0.145, 0.146, 0.147, 0.148, 0.149, 0.150, 0.151,
                0.152, 0.153, 0.154, 0.155, 0.156, 0.157, 0.158, 0.159, 0.160, 0.161, 0.162, 0.163, 0.164, 0.165, 0.166, 0.167, 0.168, 0.169, 0.170, 0.171, 0.172, 0.173, 0.174, 0.175, 0.176, 0.177,
                0.178, 0.179, 0.180, 0.181, 0.182, 0.183, 0.184, 0.185, 0.186, 0.187, 0.188, 0.189, 0.190, 0.191, 0.192, 0.193, 0.194, 0.195, 0.196, 0.197, 0.198, 0.199, 0.200, 0.201, 0.202, 0.203,
                0.204, 0.205, 0.206, 0.207, 0.208, 0.209, 0.210, 0.211, 0.212, 0.213, 0.214, 0.215, 0.216, 0.217, 0.218, 0.219, 0.220, 0.221, 0.222, 0.223, 0.224, 0.225, 0.226, 0.227, 0.228, 0.229,
                0.230, 0.231, 0.232, 0.233, 0.234, 0.235, 0.236, 0.237, 0.238, 0.239, 0.240, 0.241, 0.242, 0.243, 0.244, 0.245, 0.246, 0.247, 0.248, 0.249, 0.250, 0.251, 0.252, 0.253, 0.254, 0.255,
                0.256, 0.257, 0.258, 0.259, 0.260, 0.261, 0.262, 0.263, 0.264, 0.265, 0.266, 0.267, 0.268, 0.269, 0.270, 0.271, 0.272, 0.273, 0.274, 0.275, 0.276, 0.277, 0.278, 0.279, 0.280, 0.281,
                0.282, 0.283, 0.284, 0.285, 0.286, 0.287, 0.288, 0.289, 0.290, 0.291, 0.292, 0.293, 0.294, 0.295, 0.296, 0.297, 0.298, 0.299, 0.300, 0.301, 0.302, 0.303, 0.304, 0.305, 0.306, 0.307,
                0.308, 0.309, 0.310, 0.311, 0.312, 0.313, 0.314, 0.315, 0.316, 0.317, 0.318, 0.319, 0.320, 0.321, 0.322, 0.323, 0.324, 0.325, 0.326, 0.327, 0.328, 0.329, 0.330, 0.331, 0.332, 0.333,
                0.334, 0.335, 0.336, 0.337, 0.338, 0.339, 0.340, 0.341, 0.342, 0.343, 0.344, 0.345, 0.346, 0.347, 0.348, 0.349, 0.350, 0.351, 0.352, 0.353, 0.354, 0.355, 0.356, 0.357, 0.358, 0.359,
                0.360, 0.361, 0.362, 0.363, 0.364, 0.365, 0.366, 0.367, 0.368, 0.369, 0.370, 0.371, 0.372, 0.373, 0.374, 0.375, 0.376, 0.377, 0.378, 0.379, 0.380, 0.381, 0.382, 0.383, 0.384, 0.385,
                0.386, 0.387, 0.388, 0.389, 0.390, 0.391, 0.392, 0.393, 0.394, 0.395, 0.396, 0.397, 0.398, 0.399, 0.400, 0.401, 0.402, 0.403, 0.404, 0.405, 0.406, 0.407, 0.408, 0.409, 0.410, 0.411,
                0.412, 0.413, 0.414, 0.415, 0.416, 0.417, 0.418, 0.419, 0.420, 0.421, 0.422, 0.423, 0.424, 0.425, 0.426, 0.427, 0.428, 0.429, 0.430, 0.431, 0.432, 0.433, 0.434, 0.435, 0.436, 0.437,
                0.438, 0.439, 0.440, 0.441, 0.442, 0.443, 0.444, 0.445, 0.446, 0.447, 0.448, 0.449, 0.450, 0.451, 0.452, 0.453, 0.454, 0.455, 0.456, 0.457, 0.458, 0.459, 0.460, 0.461, 0.462, 0.463,
                0.464, 0.465, 0.466, 0.467, 0.468, 0.469, 0.470, 0.471, 0.472, 0.473, 0.474, 0.475, 0.476, 0.477, 0.478, 0.479, 0.480, 0.481, 0.482, 0.483, 0.484, 0.485, 0.486, 0.487, 0.488, 0.489,
                0.490, 0.491, 0.492, 0.493, 0.494, 0.495, 0.496, 0.497, 0.498, 0.499, 0.500, 0.501, 0.502, 0.503, 0.504, 0.505, 0.506, 0.507, 0.508, 0.509, 0.510, 0.511, 0.512, 0.513, 0.514, 0.515,
                0.516, 0.517, 0.518, 0.519, 0.520, 0.521, 0.522, 0.523, 0.524, 0.525, 0.526, 0.527, 0.528, 0.529, 0.530, 0.531, 0.532, 0.533, 0.534, 0.535, 0.536, 0.537, 0.538, 0.539, 0.540, 0.541,
                0.542, 0.543, 0.544, 0.545, 0.546, 0.547, 0.548, 0.549, 0.550, 0.551, 0.552, 0.553, 0.554, 0.555, 0.556, 0.557, 0.558, 0.559, 0.560, 0.561, 0.562, 0.563, 0.564, 0.565, 0.566, 0.567,
                0.568, 0.569, 0.570, 0.571, 0.572, 0.573, 0.574, 0.575, 0.576, 0.577, 0.578, 0.579, 0.580, 0.581, 0.582, 0.583, 0.584, 0.585, 0.586, 0.587, 0.588, 0.589, 0.590, 0.591, 0.592, 0.593,
                0.594, 0.595, 0.596, 0.597, 0.598, 0.599, 0.600, 0.601, 0.602, 0.603, 0.604, 0.605, 0.606, 0.607, 0.608, 0.609, 0.610, 0.611, 0.612, 0.613, 0.614, 0.615, 0.616, 0.617, 0.618, 0.619,
                0.620, 0.621, 0.622, 0.623, 0.624, 0.625, 0.626, 0.627, 0.628, 0.629, 0.630, 0.631, 0.633, 0.634, 0.634, 0.635, 0.636, 0.637, 0.638, 0.639, 0.640, 0.641, 0.642, 0.643, 0.644, 0.645,
                0.646, 0.647, 0.648, 0.649, 0.650, 0.650, 0.651, 0.652, 0.653, 0.654, 0.654, 0.655, 0.656, 0.657, 0.658, 0.658, 0.659, 0.660, 0.661, 0.662, 0.662, 0.663, 0.664, 0.665, 0.666, 0.667,
                0.668, 0.669, 0.670, 0.671, 0.672, 0.673, 0.674, 0.675, 0.676, 0.677, 0.678, 0.679, 0.680, 0.681, 0.682, 0.683, 0.684, 0.685, 0.686, 0.687, 0.688, 0.689, 0.690, 0.691, 0.692, 0.693,
                0.694, 0.695, 0.696, 0.697, 0.698, 0.699, 0.700, 0.701, 0.702, 0.703, 0.704, 0.705, 0.706, 0.707, 0.708, 0.709, 0.710, 0.711, 0.712, 0.713, 0.714, 0.715, 0.716, 0.717, 0.718, 0.719,
                0.720, 0.721, 0.722, 0.723, 0.724, 0.725, 0.726, 0.727, 0.728, 0.729, 0.730, 0.731, 0.732, 0.733, 0.734, 0.735, 0.736, 0.737, 0.738, 0.739, 0.740, 0.741, 0.742, 0.743, 0.744, 0.745,
                0.746, 0.747, 0.748, 0.749, 0.750, 0.751, 0.752, 0.753, 0.754, 0.755, 0.756, 0.757, 0.758, 0.759, 0.760, 0.761, 0.762, 0.763, 0.764, 0.765
        }, {306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625, 306.5625,
                309.4821429, 312.4017857, 318.2410714, 321.1607143, 324.0803571, 327.0, 332.8392857, 335.7589286, 338.6785714, 341.5982143, 347.4375, 350.3571429, 372.2544643, 416.0491071, 437.9464286,
                459.84375, 456.0482143, 448.4571429, 444.6616071, 440.8660714, 437.0705357, 433.275, 425.6839286, 421.8883929, 418.0928571, 410.5017857, 406.70625, 402.9107143, 399.1151786, 395.3196429,
                387.7285714, 383.9330357, 380.1375, 376.3419643, 368.7508929, 364.9553571, 361.1598214, 353.56875, 349.7732143, 345.9776786, 342.1821429, 334.5910714, 330.7955357, 327.0, 325.4894022,
                322.4682065, 320.9576087, 319.4470109, 317.936413, 314.9152174, 313.4046196, 311.8940217, 310.3834239, 307.3622283, 305.8516304, 304.3410326, 302.8304348, 299.8092391, 298.2986413,
                296.7880435, 295.2774457, 292.25625, 290.7456522, 289.2350543, 287.7244565, 284.7032609, 283.192663, 281.6820652, 280.1714674, 277.1502717, 275.6396739, 274.1290761, 272.6184783,
                269.5972826, 268.0866848, 266.576087, 265.9667702, 264.7481366, 264.1388199, 263.5295031, 262.9201863, 261.7015528, 261.092236, 260.4829193, 259.8736025, 258.6549689, 258.0456522,
                257.4363354, 256.8270186, 255.6083851, 254.9990683, 254.3897516, 253.7804348, 252.5618012, 251.9524845, 251.3431677, 250.7338509, 249.5152174, 248.9059006, 248.2965839, 247.6872671,
                246.4686335, 245.8593168, 245.25, 244.6910256, 243.5730769, 243.0141026, 242.4551282, 241.8961538, 240.7782051, 240.2192308, 239.6602564, 239.1012821, 237.9833333, 237.424359,
                236.8653846, 236.3064103, 235.1884615, 234.6294872, 234.0705128, 233.5115385, 232.3935897, 231.8346154, 231.275641, 230.7166667, 229.5987179, 229.0397436, 228.4807692, 227.9217949,
                226.8038462, 226.2448718, 225.6858974, 225.1269231, 224.0089744, 223.45, 222.8910256, 222.3320513, 221.2141026, 220.6551282, 220.0961538, 219.90086, 219.5102723, 219.3149785,
                219.1196847, 218.9243908, 218.5338032, 218.3385093, 218.1432155, 217.9479216, 217.557334, 217.3620401, 217.1667463, 216.9714525, 216.5808648, 216.385571, 216.1902771, 215.9949833,
                215.6043956, 215.4091018, 215.2138079, 215.0185141, 214.6279264, 214.4326326, 214.2373387, 214.0420449, 213.8467511, 213.4561634, 213.2608696, 213.2266931, 213.1583403, 213.1241639,
                213.0899875, 213.055811, 212.9874582, 212.9532818, 212.9191054, 212.8849289, 212.8165761, 212.7823997, 212.7482232, 212.7140468, 212.645694, 212.6115176, 212.5773411, 212.5431647,
                212.4748119, 212.4406355, 212.406459, 212.3722826, 212.3039298, 212.2697533, 212.2355769, 211.8195316, 210.9874408, 210.5713955, 210.1553501, 209.7393048, 208.907214, 208.4911687,
                208.0751233, 207.6590779, 206.8269872, 206.4109419, 205.9948965, 205.5788511, 204.7467604, 204.3307151, 203.9146697, 203.4986243, 203.082579, 202.2504883, 201.8344429, 201.4183975,
                200.5863068, 200.1702614, 199.7542161, 199.3381707, 198.50608, 198.0900346, 197.6739893, 197.2579439, 196.4258532, 196.0098078, 195.5937625, 195.1777171, 194.3456264, 193.929581,
                193.5135357, 193.0974903, 192.2653996, 191.8493542, 191.4333089, 191.0172635, 190.1851728, 189.7691274, 189.3530821, 188.9370367, 188.104946, 187.6889006, 187.2728552, 186.8568099,
                186.0247192, 185.6086738, 185.1926284, 184.7765831, 183.9444924, 183.528447, 183.1124016, 182.6963563, 181.8642656, 181.4482202, 181.0321748, 180.6161295, 179.7840387, 179.3679934,
                178.951948, 178.5359027, 177.7038119, 177.2877666, 176.8717212, 176.4556759, 175.6235851, 175.2075398, 174.7914944, 174.3754491, 173.5433583, 173.127313, 172.7112676, 172.6620777,
                172.5636979, 172.5145079, 172.465318, 172.4161281, 172.3177483, 172.2685583, 172.2193684, 172.1701785, 172.0717987, 172.0226088, 171.9734188, 171.9242289, 171.8258491, 171.7766592,
                171.7274692, 171.6782793, 171.5798995, 171.5307096, 171.4815197, 171.4323297, 171.3339499, 171.28476, 171.2355701, 171.1863801, 171.0880003, 171.0388104, 170.9896205, 170.9404306,
                170.8420507, 170.7928608, 170.7436709, 170.5648443, 170.207191, 170.0283643, 169.8495377, 169.6707111, 169.3130578, 169.1342312, 168.9554045, 168.7765779, 168.4189246, 168.240098,
                168.0612714, 167.8824447, 167.5247915, 167.3459648, 167.1671382, 166.9883116, 166.6306583, 166.4518317, 166.273005, 166.0941784, 165.7365251, 165.5576985, 165.3788718, 165.2000452,
                164.8423919, 164.6635653, 164.4847387, 164.305912, 163.9482588, 163.7694321, 163.5906055, 163.4117789, 163.0541256, 162.875299, 162.6964723, 162.5176457, 162.1599924, 161.9811658,
                161.8023391, 161.6235125, 161.2658592, 161.0870326, 160.908206, 160.7293793, 160.3717261, 160.1928994, 160.0140728, 159.8352462, 159.4775929, 159.2987663, 159.1199396, 158.941113,
                158.5834597, 158.4046331, 158.2258065, 158.1359054, 157.9561034, 157.8662023, 157.7763013, 157.6864003, 157.5065982, 157.4166972, 157.3267962, 157.2368952, 157.0570931, 156.9671921,
                156.8772911, 156.78739, 156.607588, 156.517687, 156.4277859, 156.3378849, 156.1580828, 156.0681818, 155.9782808, 155.8883798, 155.7085777, 155.6186767, 155.5287757, 155.4388746,
                155.2590726, 155.1691716, 155.0792705, 154.9893695, 154.8095674, 154.7196664, 154.6297654, 154.5398644, 154.3600623, 154.2701613, 154.1802603, 154.0903592, 153.9105572, 153.8206562,
                153.7307551, 153.6408541, 153.4610521, 153.371151, 153.28125, 153.2511062, 153.2209624, 153.1606748, 153.130531, 153.1003872, 153.0400996, 153.0099558, 152.9798119, 152.9496681,
                152.8893805, 152.8592367, 152.8290929, 152.7989491, 152.7386615, 152.7085177, 152.6783739, 152.6482301, 152.5879425, 152.5577987, 152.5276549, 152.4975111, 152.4372235, 152.4070796,
                152.3769358, 152.346792, 152.2865044, 152.2563606, 152.2262168, 152.196073, 152.1357854, 152.1056416, 152.0754978, 152.045354, 151.9850664, 151.9549226, 151.9247788, 151.9635351,
                152.0410477, 152.079804, 152.1185604, 152.1573167, 152.2348293, 152.2735857, 152.312342, 152.3510983, 152.4286109, 152.4673673, 152.5061236, 152.5448799, 152.6223925, 152.6611489,
                152.6999052, 152.7386615, 152.8161741, 152.8549305, 152.8936868, 152.9324431, 153.0099558, 153.0487121, 153.0874684, 153.1262247, 153.2037374, 153.2424937, 153.28125, 153.28125,
                153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125,
                153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125,
                153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125,
                153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.28125, 153.353213, 153.4971391, 153.5691021, 153.6410651,
                153.7130282, 153.8569542, 153.9289173, 154.0008803, 154.0728433, 154.2167694, 154.2887324, 154.3606954, 154.4326585, 154.5765845, 154.6485475, 154.7205106, 154.7924736, 154.9363996,
                155.0083627, 155.0803257, 155.1522887, 155.2962148, 155.3681778, 155.4401408, 155.506269, 155.6385254, 155.7046536, 155.7707818, 155.83691, 155.9691663, 156.0352945, 156.1014227,
                156.1675509, 156.2998073, 156.3659355, 156.4320637, 156.4981919, 156.6304482, 156.6965764, 156.7627046, 156.8288328, 156.9610892, 157.0272174, 157.0933455, 157.1594737, 157.2917301,
                157.3578583, 157.4239865, 157.3095681, 157.0807312, 156.9663127, 156.8518943, 156.7374759, 156.508639, 156.3942206, 156.2798021, 156.1653837, 155.9365468, 155.8221284, 155.7077099,
                155.5932915, 155.3644546, 155.2500362, 155.1356178, 155.0211993, 154.7923625, 154.677944, 154.5635256, 154.4491071, 154.2202703, 154.1058518, 153.9914334, 153.877015, 153.6481781,
                153.5337597, 153.4193412, 153.3049228, 153.1905043, 152.9616675, 152.847249, 152.7328306, 152.5039937, 152.3895753, 152.2751569, 152.1607384, 151.9319015, 151.8174831, 151.5886462,
                151.4742278, 151.3598094, 151.2453909, 151.1309725, 151.0165541, 150.7877172, 150.6732987, 150.5588803, 150.4444619, 150.215625, 150.1012066, 149.9867881, 149.8723697, 149.6435328,
                149.5291144, 149.4146959, 149.3002775, 149.1858591, 149.0714406, 148.9570222, 148.8426038, 148.7281853, 148.6137669, 148.4993485, 148.38493, 148.2705116, 148.1560931, 148.0416747,
                147.9272563, 147.8128378, 147.6984194, 147.584001, 147.4695825, 147.3551641, 147.2407457, 147.1263272, 147.0119088, 146.7830719, 146.6686535, 146.554235, 146.4398166, 146.2109797,
                146.0965613, 145.9821429, 146.5317227, 147.6308824, 148.1804622, 148.730042, 149.2796218, 150.3787815, 150.9283613, 151.4779412, 151.5380515, 151.6582721, 151.7183824, 151.7784926,
                151.8386029, 151.8987132, 152.0189338, 152.0790441, 152.1391544, 152.259375, 152.3194853, 152.3795956, 152.4397059, 152.5599265, 152.6200368, 152.6801471, 152.7402574, 152.8604779,
                152.9205882, 152.9806985, 153.0408088, 153.1610294, 153.2211397, 153.28125, 153.4515625, 153.7921875, 153.9625, 154.1328125, 154.303125, 154.64375, 154.8140625, 154.984375, 155.1546875,
                155.4953125, 155.665625, 155.8359375, 156.00625, 156.346875, 156.5171875, 156.6875, 157.1890797, 158.192239, 158.6938187, 159.1953984, 159.696978, 160.7001374, 161.201717, 161.7032967,
                162.1939725, 163.175324, 163.6659998, 164.1566755, 164.6473513, 165.6287028, 166.1193786, 166.6100543, 167.0901779, 168.0504251, 168.5305487, 169.0106723, 169.4907959, 170.4510431,
                170.9311667, 171.4112903, 171.8811985, 172.8210149, 173.2909231, 173.7608313, 174.2307395, 175.1705559, 175.6404641, 176.1103723, 176.5703877, 177.4904185, 177.9504339, 178.4104493,
                178.8704647, 179.7904955, 180.2505109, 180.7105263, 182.6654471, 186.5752886, 187.660544, 188.7457994, 189.8310549}};

        Double[] testPos = new Double[tabRaid[0].length];

        Double raid = 250.0; //raideur de la mousse
        final Date maDateDeFrappe = new Date();

        masse[0] = m2;
        vitesse[0] = 0.0;
        temps[0] = 0.0;
        position[0] = 0.0;
        mv[0] = 0.0;

        // recherche et suppression des erreurs de mesures
        for (int p = 1; p <= (nbVal - 2) / 3; p++) {
            if ((forceCapteur_1[p] > forceCapteur_1[p - 1] + 1500) || (forceCapteur_1[p] < 0)) {
                forceCapteur_1[p] = (abs(forceCapteur_1[p - 1]) + abs(forceCapteur_1[p + 1])) / 2;
            }

            if ((forceCapteur_2[p] > forceCapteur_2[p - 1] + 1500) || (forceCapteur_2[p] < 0)) {
                forceCapteur_2[p] = (abs(forceCapteur_2[p - 1]) + abs(forceCapteur_2[p + 1])) / 2;
            }
            if ((forceCapteur_3[p] > forceCapteur_3[p - 1] + 1500) || (forceCapteur_3[p] < 0)) {
                forceCapteur_3[p] = (abs(forceCapteur_3[p - 1]) + abs(forceCapteur_3[p + 1])) / 2;
            }
        }
        for (int p = (nbVal - 2) / 3; p <= 2 * (nbVal - 2) / 3; p++) {
            if ((forceCapteur_1[p] > forceCapteur_1[p - 1] + 1000) || (forceCapteur_1[p] < 0)) {
                forceCapteur_1[p] = (abs(forceCapteur_1[p - 1]) + abs(forceCapteur_1[p + 1])) / 2;
            }

            if ((forceCapteur_2[p] > forceCapteur_2[p - 1] + 1000) || (forceCapteur_2[p] < 0)) {
                forceCapteur_2[p] = (abs(forceCapteur_2[p - 1]) + abs(forceCapteur_2[p + 1])) / 2;
            }
            if ((forceCapteur_3[p] > forceCapteur_3[p - 1] + 1000) || (forceCapteur_3[p] < 0)) {
                forceCapteur_3[p] = (abs(forceCapteur_3[p - 1]) + abs(forceCapteur_3[p + 1])) / 2;
            }
        }
        for (int p = 2 * (nbVal - 2) / 3; p <= nbVal - 2; p++) {
            if ((forceCapteur_1[p] > forceCapteur_1[p - 1] + 500) || (forceCapteur_1[p] < 0)) {
                forceCapteur_1[p] = (abs(forceCapteur_1[p - 1]) + abs(forceCapteur_1[p + 1])) / 2;
            }

            if ((forceCapteur_2[p] > forceCapteur_2[p - 1] + 500) || (forceCapteur_2[p] < 0)) {
                forceCapteur_2[p] = (abs(forceCapteur_2[p - 1]) + abs(forceCapteur_2[p + 1])) / 2;
            }
            if ((forceCapteur_3[p] > forceCapteur_3[p - 1] + 500) || (forceCapteur_3[p] < 0)) {
                forceCapteur_3[p] = (abs(forceCapteur_3[p - 1]) + abs(forceCapteur_3[p + 1])) / 2;
            }
        }

        for (int p = 1; p <= nbVal - 2; p++) {
            if ((forceCapteur_1[p] > forceCapteur_1[p - 1] + 1000) || (forceCapteur_1[p] < 0)) {
                forceCapteur_1[p] = (abs(forceCapteur_1[p - 1]) + abs(forceCapteur_1[p + 1])) / 2;
            }

            if ((forceCapteur_2[p] > forceCapteur_2[p - 1] + 1000) || (forceCapteur_2[p] < 0)) {
                forceCapteur_2[p] = (abs(forceCapteur_2[p - 1]) + abs(forceCapteur_2[p + 1])) / 2;
            }
            if ((forceCapteur_3[p] > forceCapteur_3[p - 1] + 1000) || (forceCapteur_3[p] < 0)) {
                forceCapteur_3[p] = (abs(forceCapteur_3[p - 1]) + abs(forceCapteur_3[p + 1])) / 2;
            }
        }
        // Calcul du vecteur force
        for (int p = 0; p <= nbVal - 1; p++) {
            forceVecteurBrut[p] = (forceCapteur_1[p] + forceCapteur_2[p] + forceCapteur_3[p]) / 3;

            if (p == 0) {
                temps[0] = 0.0;
            } else {
                temps[p] = temps[p - 1] + dT;
            }
        }

        forceVecteurFrappe[0] = forceVecteurBrut[0];

        //Syst�me F { Makiwara + Frappe}
        for (int p = 1; p <= nbVal - 2; p++) {
            forceVecteurFrappe[p] = forceVecteurBrut[p];
            mv[p] = forceVecteurFrappe[p] * (dT);
            vitesse[p] = (-mv[p] + (masse[p - 1] + m2) * vitesse[p - 1]) / m2; //Calcul de la vitesse
            masse[p] = (mv[p] / vitesse[p]); //Calcul de la masse
            position[p] = -(vitesse[p] * temps[p + 1]);//000;//position[p-1] + (vitesse[p]*DT); //Calcul de la position
        }
        vitesse[0] = vitesse[1];

        //�stimation de la vitesse innitiale
        Double min = 1000000.0, max = -1000000.0, dif = 0.0;
        for (int p = 0; p <= nbVal - 2; p++) {
            if (vitesse[p] < min) {
                min = vitesse[p];
            }
            if (vitesse[p] > max) {
                max = vitesse[p];
            }
        }
        dif = max - min;

        vitesse[0] = vitesse[0] - min;
        //vitesse[0] = min;
        for (int p = 1; p <= nbVal - 2; p++) {
            vitesse[p] = vitesse[p] - min;
            //vitesse[p]=vitesse[p]+dif;
            //v1[p]=vitesse[p];
            masse[p] = mv[p] / vitesse[p];
            if (vitesse[p] == 0) {
                masse[p] = masse[p - 1];
            }

            double posMini = 1000;
            positionPourcent[p] = -position[p] / 0.125;

            //détermination de l'indice du minimum de différence
            for (int kk = 0; kk <= tabRaid.length - 1; kk++) {
                testPos[kk] = abs(positionPourcent[p] - tabRaid[0][kk]);
                if (testPos[kk] < posMini) {
                    posMini = testPos[kk];
                    indposMini = kk;
                }
            }

            raid = tabRaid[1][indposMini];
            fMaki_p[p] = -raid * positionPourcent[p];

            if (vitesse[p] > 2 * dif / 3) {
                fMaki_v[p] = 0.0;//(0.2*(mv[p])/(dT));
            } else if (vitesse[p] < dif / 3) {
                fMaki_v[p] = 0.0;//(0.1*(mv[p])/(dT));
            } else {
                fMaki_v[p] = 0.0;//(0.15*(mv[p])/(dT));
            }
            fMaki[p] = fMaki_p[p] + fMaki_v[p];
            Double FFFF = forceVecteurFrappe[p];
            Double FFFC = fMaki[p];
            forceVecteurFrappe[p] = forceVecteurFrappe[p] - fMaki[p];
            mv[p] = forceVecteurFrappe[p] * (dT);

            masse[p] = mv[p] / vitesse[p];
            if (vitesse[p] == 0) {
                masse[p] = masse[p - 1];
            }
        }
        punch.SetForceCapteur_1(forceCapteur_1);
        punch.SetForceCapteur_2(forceCapteur_2);
        punch.SetForceCapteur_3(forceCapteur_3);
        punch.SetVitesse(vitesse);
        punch.SetMasse(masse);
        punch.SetPosition(position);
        punch.SetDate(maDateDeFrappe);
        punch.SetForceVecteurFrappe(forceVecteurFrappe);

        precisionX3=precisionX2;
        precisionY3=precisionY2;
        precisionX2=precisionX;
        precisionY2=precisionY;
        precisionX = (punch.GetPrecisionDX()).floatValue();
        precisionY = (punch.GetPrecisionDY()).floatValue();

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)impact.getLayoutParams();
        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams)impact2.getLayoutParams();
        RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams)impact3.getLayoutParams();

        switch(i) {
            case 0:
                force.setProgress((int) (punch.GetForceMax() / 2));
                vit.setProgress((int) (punch.GetVitesseMax() / 0.01));
                puiss.setProgress((int) (punch.GetPuissanceMoyenne() / 200));
                forceVal.setText((int) (punch.GetForceMax() / 1) + "N");
                vitVal.setText(punch.GetVitesseMax() + "m/s");
                puissVal.setText((int) (punch.GetPuissanceMoyenne() / 1) + "W");
                lp.setMargins(280+(int)(100*precisionX), 280+(int)(100*precisionY), 0, 0);
                impact.setLayoutParams(lp);
                impact.setVisibility(View.VISIBLE);
                break;
            case 1:
                force2.setProgress(force.getProgress());
                vit2.setProgress(vit.getProgress());
                puiss2.setProgress(puiss.getProgress());
                force2Val.setText(forceVal.getText());
                vit2Val.setText(vitVal.getText());
                puiss2Val.setText(puissVal.getText());
                lp2.setMargins(280 + (int) (100 * precisionX2), 280 + (int) (100 * precisionY2), 0, 0);
                impact2.setLayoutParams(lp2);
                impact2.setVisibility(View.VISIBLE);

                force.setProgress((int) (punch.GetForceMax() / 2));
                vit.setProgress((int) (punch.GetVitesseMax() / 0.01));
                puiss.setProgress((int) (punch.GetPuissanceMoyenne() / 200));
                forceVal.setText((int) (punch.GetForceMax() / 1) + "N");
                vitVal.setText(punch.GetVitesseMax() + "m/s");
                puissVal.setText((int)(punch.GetPuissanceMoyenne()/1)+"W");
                lp.setMargins(280 + (int) (100 * precisionX), 280 + (int) (100 * precisionY), 0, 0);
                impact.setLayoutParams(lp);
                break;
            default:
                force3.setProgress(force2.getProgress());
                vit3.setProgress(vit2.getProgress());
                puiss3.setProgress(puiss2.getProgress());
                force3Val.setText(force2Val.getText());
                vit3Val.setText(vit2Val.getText());
                puiss3Val.setText(puiss2Val.getText());
                lp3.setMargins(280 + (int) (100 * precisionX3), 280 + (int) (100 * precisionY3), 0, 0);
                impact3.setLayoutParams(lp3);
                impact3.setVisibility(View.VISIBLE);

                force2.setProgress(force.getProgress());
                vit2.setProgress(vit.getProgress());
                puiss2.setProgress(puiss.getProgress());
                force2Val.setText(forceVal.getText());
                vit2Val.setText(vitVal.getText());
                puiss2Val.setText(puissVal.getText());
                lp2.setMargins(280+(int)(100*precisionX2), 280+(int)(100*precisionY2), 0, 0);
                impact2.setLayoutParams(lp2);

                force.setProgress((int) (punch.GetForceMax() / 2));
                vit.setProgress((int) (punch.GetVitesseMax() / 0.01));
                puiss.setProgress((int) (punch.GetPuissanceMoyenne() / 200));
                forceVal.setText((int) (punch.GetForceMax() / 1) + "N");
                vitVal.setText(punch.GetVitesseMax() + "m/s");
                puissVal.setText((int)(punch.GetPuissanceMoyenne()/1)+"W");
                lp.setMargins(280 + (int) (100 * precisionX), 280 + (int) (100 * precisionY), 0, 0);
                impact.setLayoutParams(lp);
                break;
        }


        handler2.sendEmptyMessage(OK);
        running = true;
        i+=1;

        punch.SetForceCapteur_1(null);
        punch.SetForceCapteur_2(null);
        punch.SetForceCapteur_3(null);
        punch.SetVitesse(null);
        punch.SetMasse(null);
        punch.SetPosition(null);
        punch.SetDate(null);
        punch.SetForceVecteurFrappe(null);

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
            Data = Data + msg.getData().getString("receivedData");
            if ((Data != "") && (dejaAffiche == false)) {
                dejaAffiche = true;
            }
            byte[] b = Data.getBytes();
            String[] toto = Data.split(":");

            if (toto.length >= 3)
            {
                String test = toto[2];

                if ((test.charAt(test.length() - 1) == '/') && (toto.length == 4)){//
                    running = false;
                    String toto1 = toto[0];
                    String[] CtempsString1 = toto1.split(";");
                    Double[] pCapteur1 = new Double[CtempsString1.length];

                    String toto2 = toto[1];
                    String[] CtempsString2 = toto2.split(";");
                    Double[] pCapteur2 = new Double[CtempsString2.length];

                    String toto3 = toto[2];
                    String[] CtempsString3 = toto3.split(";");
                    Double[] pCapteur3 = new Double[CtempsString3.length - 1];

                    //Tester si il n y a que des chiffres
                    for (int ii = 0; ii <= CtempsString1.length - 1; ii++) {

                        char[] CtempsChar1 = CtempsString1[ii].toCharArray();
                        for(int jj = 0; jj <CtempsChar1.length; jj++){
                            boolean estUnChiffre = Character.isDigit(CtempsChar1[jj]);
                            if (estUnChiffre == false){//||(CtempsChar1.equals("")
                                queDesChiffres = false;
                            }
                        }
                    }


                    for (int ii = 0; ii <= CtempsString2.length - 1; ii++) {
                        char[] CtempsChar2 = CtempsString2[ii].toCharArray();
                        for(int jj = 0; jj <CtempsChar2.length; jj++){
                            boolean estUnChiffre = Character.isDigit(CtempsChar2[jj]);
                            if (estUnChiffre == false){
                                queDesChiffres = false;
                            }
                        }
                    }
                    for (int ii = 0; ii <= CtempsString3.length - 2; ii++) {
                        char[] CtempsChar3 = CtempsString3[ii].toCharArray();
                        for(int jj = 0; jj <CtempsChar3.length; jj++){
                            boolean estUnChiffre = Character.isDigit(CtempsChar3[jj]);
                            if (estUnChiffre == false){
                                queDesChiffres = false;
                            }
                        }
                    }

                    //Reprise du traitement si il n'y a que des chiffres
                    if (queDesChiffres == true){

                        Double w1 = 0.0;
                        Double w2 = 0.0;
                        Double w3 = 0.0;
                        for (int ii = 0; ii <= CtempsString1.length - 1; ii++) {
                            if ((CtempsString1[ii] != "") || (CtempsString2[ii] != "")){
                                try {
                                    w1 = new Double(CtempsString1[ii].toString());
                                    w2 = new Double(CtempsString2[ii].toString());
                                } catch (NumberFormatException e) {
                                    w1 = 1/bb; // your default value
                                    w2 = 1/bb;
                                }
                                pCapteur1[ii] = (((1 / w1)) - bb) / aa1;
                                pCapteur2[ii] = (((1 / w2)) - bb) / aa2;
                            }
                        }

                        for (int ii = 0; ii <= CtempsString3.length - 2; ii++) {
                            if (CtempsString1[ii] != "") {
                                try {
                                    w3 = new Double(CtempsString1[ii].toString());
                                } catch (NumberFormatException e) {
                                    w3 = 1/bb; // your default value
                                }
                                pCapteur3[ii] = ((1 / w3) - bb) / aa3;
                            }
                        }


                        Simulation(pCapteur1, pCapteur2, pCapteur3);

                        Data = "";
                        if ((Data == "") && (dejaAffiche2 == false)) {

                            dejaAffiche2 = true;
                        }
                        //Toast.makeText(IntentCompetition.this, "Réception", Toast.LENGTH_SHORT).cancel();
                        //startActivity(IntentIHMFrappe);
                    }
                    else if(queDesChiffres == false){
                        handler2.sendEmptyMessage(NOK);
                        running = true;
                        Data = "";
                        queDesChiffres = true;
                        for(int jj = 0; jj <toto.length; jj++) {
                            toto[jj] = "";
                        }
                    }
                }
            }
        }
    };

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case OK:
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //vert.setVisibility(View.VISIBLE);
                            //rouge.setVisibility(View.INVISIBLE);
                        }
                    });
                    beginListenForData();
                    break;
                case NOK:
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //vert.setVisibility(View.INVISIBLE);
                            //rouge.setVisibility(View.VISIBLE);
                        }
                    });
                    break;
            }
        }
    };

}
