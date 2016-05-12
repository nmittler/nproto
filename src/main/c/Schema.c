#include <jni.h>
#include <stdio.h>
//#include "Schema.h"

JNIEXPORT void JNICALL Java_Schema_writeTo
          (JNIEnv *env, jobject thisObj, jobject message, jobject writer, jlongArray schemaData) {
   printf("Hello World!\n");
   return;
}
