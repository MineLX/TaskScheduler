package main;

public class CallbackTask {

	private final Object completionLock;

	private final Object taskIndexLock;

	private final int taskCount;

	private int taskIndex;

	public CallbackTask() {
		this(1);
	}

	public CallbackTask(int count) {
		this.taskCount = count;

		completionLock = new Object();
		taskIndexLock = new Object();
	}

	public boolean isDone() {
		return taskIndex == taskCount;
	}

	public void done() {
		if (isDone())
			return;

		progressUp();

		// notify waiter
		synchronized (completionLock) {
			completionLock.notify();
		}
	}

	public void waitForCompletion() throws InterruptedException {
		if (isDone())
			return;

		// wait for completion notifier
		synchronized (completionLock) {
			while (!isDone()) {
				completionLock.wait();
			}
		}
	}

	public int completedCount() {
		return taskIndex;
	}

	private void progressUp() {
		synchronized (taskIndexLock) {
			taskIndex++;
		}
	}
}
