const SPREADSHEET_ID = '1BcO2b5NmbRw5eIMdcHVnjTY2AlGLx8-PW_3F4EBGvwQ';
const SHEET_NAME = 'Feedback Responses';

function doPost(e) {
  const lock = LockService.getScriptLock();
  lock.waitLock(10000);

  try {
    const payload = JSON.parse(e.postData.contents || '{}');
    const sheet = getFeedbackSheet_();
    const ratings = payload.ratings || {};

    sheet.appendRow([
      new Date(),
      payload.submittedAt || '',
      payload.clientName || '',
      Array.isArray(payload.services) ? payload.services.join(', ') : '',
      payload.otherService || '',
      ratings['The level of courtesy and professionalism of our staff.'] || '',
      ratings['The waiting time of service.'] || '',
      ratings['Overall services provided.'] || '',
      ratings['The information provided by our staff.'] || '',
      ratings['Resolution of your concern / complaints.'] || '',
      ratings['Cleanliness of the Rest Room areas.'] || '',
      ratings['Cleanliness of waiting area.'] || '',
      ratings['Coolness of Service Area.'] || '',
      payload.comments || ''
    ]);

    return json_({ ok: true });
  } catch (error) {
    return json_({ ok: false, error: String(error) });
  } finally {
    lock.releaseLock();
  }
}

function getFeedbackSheet_() {
  const spreadsheet = SpreadsheetApp.openById(SPREADSHEET_ID);
  let sheet = spreadsheet.getSheetByName(SHEET_NAME);

  if (!sheet) {
    sheet = spreadsheet.insertSheet(SHEET_NAME);
  }

  if (sheet.getLastRow() === 0) {
    sheet.appendRow([
      'Timestamp',
      'Device Submitted At',
      'Client Name',
      'Services',
      'Other Service',
      'Courtesy / Professionalism',
      'Waiting Time',
      'Overall Services',
      'Information Provided',
      'Resolution',
      'Rest Room Cleanliness',
      'Waiting Area Cleanliness',
      'Coolness of Service Area',
      'Comments'
    ]);
  }

  return sheet;
}

function json_(value) {
  return ContentService
    .createTextOutput(JSON.stringify(value))
    .setMimeType(ContentService.MimeType.JSON);
}
