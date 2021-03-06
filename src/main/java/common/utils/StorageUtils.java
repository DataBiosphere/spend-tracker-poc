package common.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.config.ServiceAccountSpecification;

public class StorageUtils {
  private static final Logger logger = LoggerFactory.getLogger(StorageUtils.class);

  private StorageUtils() {}

  /**
   * Build a Google Storage client object with credentials for the given service account. The client
   * object is newly created on each call to this method; it is not cached.
   *
   * <p>This method uses the default project, typically as set by the environment variable
   * GOOGLE_CLOUD_PROJECT,
   */
  public static Storage getClientForServiceAccount(ServiceAccountSpecification serviceAccount)
      throws Exception {
    String defaultProjectId = StorageOptions.getDefaultProjectId();
    return getClientForServiceAccount(serviceAccount, defaultProjectId);
  }

  /**
   * Build a Google Storage client object with credentials for the given service account and
   * project. The client object is newly created on each call to this method; it is not cached.
   */
  public static Storage getClientForServiceAccount(
      ServiceAccountSpecification serviceAccount, String projectId) throws Exception {
    logger.debug(
        "Fetching credentials and building Storage client object for service account: {}, project: {}",
        serviceAccount.name,
        projectId);

    GoogleCredentials serviceAccountCredentials =
        AuthenticationUtils.getServiceAccountCredential(serviceAccount);
    StorageOptions storageOptions =
        StorageOptions.newBuilder()
            .setCredentials(serviceAccountCredentials)
            .setProjectId(projectId)
            .build();
    Storage storageClient = storageOptions.getService();

    return storageClient;
  }

  /**
   * Write the contents of a byte array to a file in the given GCS bucket.
   *
   * @param byteArray the bytes to write
   * @param fileName the name of the file
   * @param bucketName the bucket to write to
   * @return the created BlobId
   */
  public static BlobId writeBytesToFile(
      Storage storageClient, String bucketName, String fileName, byte[] byteArray) {
    BlobInfo blobToCreate = BlobInfo.newBuilder(bucketName, fileName).build();
    Blob createdBlob = storageClient.create(blobToCreate, byteArray);

    return createdBlob.getBlobId();
  }

  /** Convert a given BlobId to a gs:// path. Does not check for existence. */
  public static String blobIdToGSPath(BlobId blobId) {
    return String.format("gs://%s/%s", blobId.getBucket(), blobId.getName());
  }

  /** Delete all files in the given list. */
  public static void deleteFiles(Storage storageClient, List<BlobId> files) {
    for (BlobId file : files) {
      Blob blob = storageClient.get(file);
      if (blob == null) {
        logger.debug(
            "Blob not found: bucket = {}, file name = {}", file.getBucket(), file.getName());
      } else {
        logger.debug(
            "Deleting blob: bucket = {}, file name = {}", file.getBucket(), file.getName());
        blob.delete();
      }
    }
  }
}
