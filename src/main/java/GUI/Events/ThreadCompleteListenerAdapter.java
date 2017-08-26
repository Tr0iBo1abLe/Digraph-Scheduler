package GUI.Events;

import GUI.Frame.view.Controller;
import GUI.Interfaces.ThreadCompleteListener;
import GUI.Models.SysInfoModel;

/**
 * This class implements ThreadCompleteListener
 * 
 * @author Vincent
 * @see ThreadCompleteListener
 */
public class ThreadCompleteListenerAdapter implements ThreadCompleteListener {
	private Controller controller;

	public ThreadCompleteListenerAdapter(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void notifyOfSysInfoThreadUpdate() {
		updateSysInfo(SysInfoModel.getInstance());
	}

	public void updateSysInfo(SysInfoModel sysInfoModel) {
		double cpu = sysInfoModel.getCpuPercentage();
		double mem = sysInfoModel.getMem().getUsedPercent();
		if ((cpu != Double.NaN) && (mem != Double.NaN)) {
			controller.setCpu(sysInfoModel.getCpuPercentage());
			controller.setRam(sysInfoModel.getMem().getUsedPercent());
		}
	}

	@Override
	public void notifyOfSolversThreadComplete() {
		controller.completed();
	}
}
