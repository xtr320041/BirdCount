package nz.govt.doc.birdcount;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

class SendMail extends AsyncTask<String, Integer, Void> {

	protected void onProgressUpdate() {
		// called when the background task makes any progress
	}

	protected void onPreExecute() {
		// called before doInBackground() is started
	}

	protected void onPostExecute() {
		// called after doInBackground() has finished
	}

	@Override
	protected Void doInBackground(String... filePath) {
		// TODO Auto-generated method stub
		try {
			sendAll(filePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void sendAll(String[] filePath) throws Exception {
		Mail m = new Mail();

		String[] toArr = { "doctest.nz@gmail.com" };
		m.setTo(toArr);
		// m.setFrom("wooo@wooo.com");
		// m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
		m.setBody("Email body.");
		// String filePath =
		// Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
		// APPDIR + "/" + DATADIR + "/" + BIRDDATAFILE;
		m.addAttachment(filePath[0]);

		m.send();

	}

}
