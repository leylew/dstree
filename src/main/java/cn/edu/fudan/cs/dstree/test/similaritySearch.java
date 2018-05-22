package cn.edu.fudan.cs.dstree.test;

import cn.edu.fudan.cs.dstree.dynamicsplit.IndexExactSearcher;
import cn.edu.fudan.cs.dstree.dynamicsplit.Node;
import cn.edu.fudan.cs.dstree.util.DistUtil;
import cn.edu.fudan.cs.dstree.util.TimeSeriesFileUtil;
import cn.edu.fudan.cs.dstree.util.TimeSeriesReader;

import java.io.IOException;

public class similaritySearch {
    /**最近邻查询改成阈值查询*/
    public static void main(String[] args) throws IOException {
        double[][] qTss = TimeSeriesFileUtil.readSeriesFromBinaryFileAtOnce("data/data1",2000);
        //double[] Ts = new double[]{0.8645,1.1007,1.0787,1.0149,1.1787,0.9234,1.1826,1.1492,1.4364,0.9425,1.1199,1.1190,0.8804,1.1023,0.9605,1.0566,1.0122,1.0619,0.8432,1.1143,1.2843,-0.3788,-0.4057,-0.3954,-0.4042,-0.4045,-0.3978,-0.3877,-0.4275,-0.3913,-0.4159,-0.3997,-0.3974,-0.3960,-0.3927,-0.3942,-0.3979,-0.4065,-0.4153,-1.3201,-1.2590,-1.2931,-1.2287,-1.1721,-1.4705,-1.3253,-1.0405,-0.8823,-1.0478,-1.3916,-1.1882,-0.6878,-0.9706,-1.0852,-1.1269,-0.1072,-1.2461,-0.8982,2.2654,1.9751,-0.5709,-0.5085,1.1291,1.2332};
        String indexFileName = "data/Series_64_20000.z.txt.idx_dyn_100_1" + "/" + "root.idx";
        //String indexFileName = "data/data.idx_dyn_100_1/root.idx";
        Node root = null;
        try {
            root = Node.loadFromFile(indexFileName);
            double[] rTs = findMostSimilarTs(qTss[1],root);
            thresholdSearch(qTss[1], root, 50.0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static double[] findMostSimilarTs(double[] ts,Node node){
        double[] rTs = new double[]{0.0};
        try {
            Node rNode = IndexExactSearcher.exactSearch(ts,node);
            double[][] tss = TimeSeriesFileUtil.readSeriesFromBinaryFileAtOnce(rNode.getFileName(),ts.length);
            rTs = DistUtil.minDistTs(tss, ts);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rTs;
    }
    public static void thresholdSearch(double[] ts, Node node, double threshold){

    }
}
