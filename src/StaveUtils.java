import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Stave Utils Class used for OpenCV modification and algorithms application on a stave input image.
 * <p>
 * Creator: Ambrozie
 * Info: StaveUtils.class
 * Date: 12/06/2017 01:12
 */
public class StaveUtils {
    private static final String OUTPUT_FOLDER_NAME = "C:\\Users\\Ambrozie\\IdeaProjects\\OpenCVStart\\outputs";
    private static final String GRAY_IMAGE_PATH = OUTPUT_FOLDER_NAME + "\\0_gray.png";
    private static final String BITWISE_NOT_IMAGE_PATH = OUTPUT_FOLDER_NAME + "\\1_bitwise_not.png";
    private static final String BINARY_IMAGE_PATH = OUTPUT_FOLDER_NAME + "\\2_binary.png";
    private static final String HORIZONTAL_OBJ_IMAGE_PATH = OUTPUT_FOLDER_NAME + "\\3_horizontal.png";
    private static final String VERTICAL_OBJ_IMAGE_PATH = OUTPUT_FOLDER_NAME + "\\4_vertical.png";

    /**
     * For presentation purposes!
     *
     * @param imagePath input image full path
     */
    public void run(String imagePath) {
        System.out.println("Loading image");
        Mat staveMat = Imgcodecs.imread(imagePath);

        // Change input Mat to GRAY if not already
        if (staveMat.channels() == 3) {
            Imgproc.cvtColor(staveMat, staveMat, Imgproc.COLOR_BGR2GRAY);
        }
        saveImage(staveMat, GRAY_IMAGE_PATH);

        // BITWISE_NOT of the GRAY input image
        Core.bitwise_not(staveMat, staveMat);
        saveImage(staveMat, BITWISE_NOT_IMAGE_PATH);

        Mat binaryMat = getBinaryMat(staveMat);
        Mat horizontalObjectsMat = getHorizontalObjectsMat(binaryMat);
        Mat verticalObjectsMat = getVerticalObjectsMat(binaryMat);

        saveImage(binaryMat, BINARY_IMAGE_PATH);
        saveImage(horizontalObjectsMat, HORIZONTAL_OBJ_IMAGE_PATH);
        saveImage(verticalObjectsMat, VERTICAL_OBJ_IMAGE_PATH);
    }

    /**
     * Get BINARY Mat from an input GRAY Mat
     *
     * @param staveMat input GRAY Mat
     * @return binary MAT
     */
    private Mat getBinaryMat(Mat staveMat) {
        Mat binaryStaveMat = new Mat();
        int blockSize = 15;
        int constant = -2;
        Imgproc.adaptiveThreshold(staveMat, binaryStaveMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, blockSize, constant);

        return binaryStaveMat;
    }

    /**
     * Get Mat that contains the horizontal objects withing a binary Mat
     *
     * @param staveMat the binary input Mat
     * @return Mat containing only the horizontal objects
     */
    private Mat getHorizontalObjectsMat(Mat staveMat) {
        Mat horizontalObjectsMat = staveMat.clone();
        int horizontalSize = horizontalObjectsMat.cols() / 30;
        Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontalSize, 1));

        // defaults: Point(-1,-1), iterations = 1
        Imgproc.erode(horizontalObjectsMat, horizontalObjectsMat, horizontalStructure);
        Imgproc.dilate(horizontalObjectsMat, horizontalObjectsMat, horizontalStructure);

        return horizontalObjectsMat;
    }

    /**
     * Get Mat that contains the vertical objects withing a binary Mat
     *
     * @param staveMat the binary input Mat
     * @return Mat containing only the vertical objects
     */
    private Mat getVerticalObjectsMat(Mat staveMat) {
        Mat verticalObjectsMat = staveMat.clone();
        int verticalSize = verticalObjectsMat.rows() / 30;
        Mat vertialStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, verticalSize));

        // defaults: Point(-1,-1), iterations = 1
        Imgproc.erode(verticalObjectsMat, verticalObjectsMat, vertialStructure);
        Imgproc.dilate(verticalObjectsMat, verticalObjectsMat, vertialStructure);

        return verticalObjectsMat;
    }

    /**
     * Save the Mat object as a .png
     *
     * @param mat  Mat to save
     * @param file full file path
     */
    private void saveImage(Mat mat, String file) {
        System.out.println(String.format("Saving Mat to png: %s", file));
        Imgcodecs.imwrite(file, mat);
    }
}