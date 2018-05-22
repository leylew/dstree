package cn.edu.fudan.cs.dstree.dynamicsplit;

import cn.edu.fudan.cs.dstree.util.TimeSeriesFileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ThresholdSearch {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int index_threshold = 10;
        int fileLength = 1000000;
        String indexPath = "data/1000000d.idx_dyn_" + index_threshold + "_1";
        String indexFileName = indexPath.concat("/root.idx");
        //File file = new File(indexPath);
        Node newRoot = Node.loadFromFile(indexFileName);
        double[] qTss = TimeSeriesFileUtil.readSeriesFromFile("data/1000000d",0);
        double threshold = 10;
        for(int i = 0; i < qTss.length; i ++) System.out.print(qTss[i] + " ");
        System.out.println();
        List<Node> resultNodes = thresholdSearch(qTss, newRoot, threshold);
        int finalCount = 0;
        for(int i = 0; i < resultNodes.size(); i ++){
            double[][] tss = TimeSeriesFileUtil.readSeriesFromBinaryFileAtOnce(resultNodes.get(i).getFileName(), qTss.length);
            for(int j = 0; j < tss.length; j ++){
                if(CalcDist(tss[j],qTss) <= threshold)finalCount ++;
            }
        }
        System.out.println("node count: " + resultNodes.size());
        System.out.println("final count: " + finalCount);
    }

    private static double CalcDist(double[] tss, double[] qTss) {
        double dist = 0;
        for(int i = 0; i < tss.length; i ++){
            dist += (tss[i] - qTss[i]) * (tss[i] - qTss[i]);
        }
        System.out.println(Math.sqrt(dist));
        return Math.sqrt(dist);
    }

    private static List<Node> thresholdSearch(double[] ts, Node currentNode, double threshold) {
        List<Node> result = new ArrayList();
        int count = 0;
        BlockingQueue<Node> queue = new LinkedBlockingDeque<Node>();
        queue.add(currentNode);
        while(!queue.isEmpty()){
            try {
                Node cNode = queue.take();
                if(cNode.isTerminal()){
                    result.add(cNode);
                    count += cNode.size;
                }
                else {
                    Node rNode = cNode.right;
                    Node lNode = cNode.left;
                    if(rNode != null && rNode.satisfyThreshold(ts, threshold)) queue.add(rNode);
                    if(lNode != null && lNode.satisfyThreshold(ts, threshold)) queue.add(lNode);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("series count: " + count);
        return result;
    }
}
