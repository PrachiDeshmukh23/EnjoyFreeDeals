# EnjoyFreeDeals Firebase Setup

The Android project is already wired for Firebase Authentication, Cloud Firestore, Firebase Cloud Messaging, and the Google Services Gradle plugin.

## 1. Create Firebase Project

1. Open Firebase Console.
2. Create a project named `EnjoyFreeDeals`.
3. Add an Android app.
4. Use this package name:

```text
com.bizflow.enjoyfreedeals
```

5. Download the generated `google-services.json`.
6. Put it here:

```text
app/google-services.json
```

Do not rename `app/google-services.json.example`; it is only a placeholder showing the expected package name. The real file must come from Firebase.

## 2. Enable Authentication

In Firebase Console:

1. Go to Authentication.
2. Open Sign-in method.
3. Enable Email/Password.
4. Optional: enable Google if you want to replace the current Google button message with real Google sign-in.

If `app/google-services.json` is missing, login and registration show:

```text
Firebase is not configured. Please add google-services.json.
```

## 3. Enable Firestore

1. Go to Cloud Firestore.
2. Create a database.
3. Start in production mode.
4. Add the rules from:

```text
firestore.rules
```

## 4. Create Collections

Create these collections:

```text
deals
categories
videos
users
notifications
```

The sample documents are in:

```text
firestore_sample_data.json
```

Use each object's `id` as the Firestore document ID.

## 5. Firestore Fields

`deals`

```text
id, title, description, imageUrl, originalPrice, discountedPrice,
discountPercentage, storeName, storeLogoUrl, categoryId, dealUrl,
isHotDeal, isFreeDeal, isLimitedTime, expiryDate, createdAt
```

`categories`

```text
id, name, iconUrl, gradientStart, gradientEnd
```

`videos`

```text
id, title, description, imageUrl, videoUrl, createdAt
```

`users`

```text
uid, name, email, savedDeals, createdAt
```

`notifications`

```text
id, title, message, dealId, targetUrl, isRead, createdAt
```

## 6. Enable Cloud Messaging

Firebase Cloud Messaging is already added in:

```text
app/src/main/java/com/bizflow/enjoyfreedeals/notification/EnjoyFirebaseMessagingService.kt
```

The service is registered in:

```text
app/src/main/AndroidManifest.xml
```

## 7. Build

After adding the real `app/google-services.json`, build the app from Android Studio or run:

```text
gradlew assembleDebug
```

Firestore-backed screens keep built-in sample data as a development fallback, but real sign-in requires Firebase Authentication to be configured.
