/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include "crapto1.h"

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
uint64_t
Java_tw_edu_ntu_proxmarkandroid_MainActivity_stringFromJNI( JNIEnv* env,
                                                  jobject thiz, uint64_t uid, uint64_t tag_challenge, uint64_t nr_enc
                                                  , uint64_t reader_response, uint64_t tag_response)
{
	struct Crypto1State *revstate;
	uint64_t lfsr;
	unsigned char* plfsr = (unsigned char*)&lfsr;

	uid &= 0xffffffff;
	tag_challenge &= 0xffffffff;
	nr_enc &= 0xffffffff;
	reader_response &= 0xffffffff;
	tag_response &= 0xffffffff;

	uint32_t ks2 = reader_response ^ prng_successor(tag_challenge, 64);
	uint32_t ks3 = tag_response ^ prng_successor(tag_challenge, 96);

	revstate = lfsr_recovery64(ks2, ks3);
	lfsr_rollback_word(revstate, 0, 0);
	lfsr_rollback_word(revstate, 0, 0);
	lfsr_rollback_word(revstate, nr_enc, 1);
	lfsr_rollback_word(revstate, uid ^ tag_challenge, 0);
	crypto1_get_lfsr(revstate, &lfsr);

	return lfsr;

}