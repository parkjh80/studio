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

package org.apache.parquet.hadoop;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.filter2.compat.FilterCompat;
import org.apache.parquet.hadoop.api.ReadSupport;
import org.apache.parquet.hadoop.util.HiddenFileFilter;

public class BrighticsParquetReader<T> extends ParquetReader<T> {

	private final ReadSupport<T> readSupport;
	private final Configuration conf;
	private final Iterator<Footer> footersIterator;
	private InternalParquetRecordReader<T> reader;

	public BrighticsParquetReader(Configuration conf, Path file, ReadSupport<T> readSupport) throws IOException {
		super(file, readSupport);
		FileSystem fs = file.getFileSystem(conf);
		List<FileStatus> statuses = Arrays.asList(fs.listStatus(file, HiddenFileFilter.INSTANCE));
		List<Footer> footers = ParquetFileReader.readAllFootersInParallelUsingSummaryFiles(conf, statuses, false);
		this.conf = conf;
		this.readSupport = readSupport;
		this.footersIterator = footers.iterator();
	}
	
	public T read(int[] filteredColumns) throws IOException {
		try {
			if (reader != null && reader.nextKeyValue(filteredColumns)) {
				return reader.getCurrentValue();
			} else {
				initReader();
				return reader == null ? null : read(filteredColumns);
			}
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}

	public T read() throws IOException {
		try {
			if (reader != null && reader.nextKeyValue()) {
				return reader.getCurrentValue();
			} else {
				initReader();
				return reader == null ? null : read();
			}
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}

	private void initReader() throws IOException {
		if (reader != null) {
			reader.close();
			reader = null;
		}
		if (footersIterator.hasNext()) {
			Footer footer = footersIterator.next();
			ParquetFileReader fileReader = ParquetFileReader.open(conf, footer.getFile(), footer.getParquetMetadata());
			fileReader.filterRowGroups(FilterCompat.NOOP);
			reader = new InternalParquetRecordReader<T>(readSupport, FilterCompat.NOOP);
			reader.initialize(fileReader, conf);
		}
	}

}
