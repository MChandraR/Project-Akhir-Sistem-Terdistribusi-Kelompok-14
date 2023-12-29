const express = require('express');
const multer = require('multer');
const multerS3 = require('multer-s3');
const AWS = require('aws-sdk');

const {
  S3,
} = require('@aws-sdk/client-s3');

const app = express();
const port = 3000;

// Configure AWS
// JS SDK v3 does not support global configuration.
// Codemod has attempted to pass values to each service client in this file.
// You may need to update clients outside of this file, if they use global config.
// AWS.config.update({
//   credentials: {
//     accessKeyId: 'ASIAZCK5QANDKGBVUEF5',
//     secretAccessKey: 'sPY3d6t/9F4SrA/uRs9lNmDOZP7S2CIR35DuyWaq',
//   },
//   region: 'eu-central-1',
// });

const s3 = new S3();

// Configure Multer with S3 storage
const upload = multer({
  storage: multerS3({
    s3: s3,
    bucket: 'cyclic-olive-pelican-belt-eu-central-1',
    metadata: (_, file, cb) => {
      cb(null, { fieldName: file.fieldname });
    },
    key: (_, file, cb) => {
      cb(null,file.originalname);
    },
  }),
});

// Define the route to handle file upload
app.post('/upload', upload.single('file'), (req, res) => {
  // Access uploaded file details in req.file
  res.json({ message: 'File uploaded successfully', file: req.file });
});

// Start the server
app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
