"""
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
"""

from brightics.common.repr import BrtcReprBuilder, strip_margin, plt2MD, \
    pandasDF2MD, keyValues2MD
from brightics.common.groupby import _function_by_group
from brightics.common.utils import check_required_parameters
from brightics.common.utils import get_default_from_parameters_if_required
from brightics.common.validation import validate
from brightics.common.validation import from_under

import pandas as pd
import numpy as np
from scipy.stats import kruskal


def kruskal_wallis_test(table, group_by=None, **params):
    check_required_parameters(_kruskal_wallis_test, params, ['table'])
    
    params = get_default_from_parameters_if_required(params, _kruskal_wallis_test)
    
    if group_by is not None:
        return _function_by_group(_kruskal_wallis_test, table, group_by=group_by, **params)
    else:
        return _kruskal_wallis_test(table, **params)


def _kruskal_wallis_test(table, response_cols, factor_col, nan_policy='propagate'):
    result = dict()
    rb = BrtcReprBuilder()
    rb.addMD("""## Kruskal Wallis test Result""")
    
    groups = dict()
    for name, group in table.groupby(factor_col):
        groups[name] = group
        
    for response_col in response_cols:
        stats, pval = kruskal(*[x[response_col] for x in groups.values()])
        rb.addMD(strip_margin("""
        | ## {response_col} by {factor_col}
        |
        | ### Statistics value: {stats}
        |
        | ### P value: {pval}
        """.format(response_col=response_col, factor_col=factor_col, stats=stats, pval=pval)))
            
        name = response_col + '_' + factor_col
        result[name] = dict()
        result[name]['Statistics'] = stats
        result[name]['P value'] = pval
        
    result['_repr_brtc_'] = rb.get()
        
    return {'result': result}
