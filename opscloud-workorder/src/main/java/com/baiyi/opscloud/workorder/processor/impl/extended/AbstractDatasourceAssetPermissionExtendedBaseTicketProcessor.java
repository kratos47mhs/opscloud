package com.baiyi.opscloud.workorder.processor.impl.extended;

import com.baiyi.opscloud.domain.base.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;

/**
 * @Author baiyi
 * @Date 2022/1/7 2:07 PM
 * @Version 1.0
 */
public abstract class AbstractDatasourceAssetPermissionExtendedBaseTicketProcessor extends BaseTicketProcessor<DatasourceInstanceAsset> implements IInstanceType {

    @Override
    protected Class<DatasourceInstanceAsset> getEntryClassT() {
        return DatasourceInstanceAsset.class;
    }

}