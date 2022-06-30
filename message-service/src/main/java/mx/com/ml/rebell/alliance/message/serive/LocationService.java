package mx.com.ml.rebell.alliance.message.serive;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LocationService {

  public double[] getLocation(final double[][] positions, final double [] distances) {
    log.info("Calculate position of alliance");
    var trilaterationFunction = new TrilaterationFunction(positions, distances);
    var squareSolver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());
    return squareSolver.solve().getPoint().toArray();
  }
}
