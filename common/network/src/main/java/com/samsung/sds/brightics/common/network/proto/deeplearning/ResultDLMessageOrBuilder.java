/*
    Copyright 2019 Samsung SDS
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: deeplearning.proto

package com.samsung.sds.brightics.common.network.proto.deeplearning;

public interface ResultDLMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.samsung.sds.brightics.common.network.proto.deeplearning.ResultDLMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.com.samsung.sds.brightics.common.network.proto.MessageStatus messageStatus = 1;</code>
   */
  int getMessageStatusValue();
  /**
   * <code>.com.samsung.sds.brightics.common.network.proto.MessageStatus messageStatus = 1;</code>
   */
  com.samsung.sds.brightics.common.network.proto.MessageStatus getMessageStatus();

  /**
   * <code>.google.protobuf.Any result = 2;</code>
   */
  boolean hasResult();
  /**
   * <code>.google.protobuf.Any result = 2;</code>
   */
  com.google.protobuf.Any getResult();
  /**
   * <code>.google.protobuf.Any result = 2;</code>
   */
  com.google.protobuf.AnyOrBuilder getResultOrBuilder();
}
