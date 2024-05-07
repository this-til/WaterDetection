import axios, {AxiosResponse} from 'axios';

const apiString = () => {return "/api"};
const api = axios.create({
    headers: {
        'Content-Type': 'application/json',
    },
    baseURL: apiString(),
});

export const CommandApi = {
    registerCommand: (ruleId: number, actuatorId: number, commandTrigger: number): Promise<AxiosResponse<Result<void>>> => api.post('/command/registerCommand', {
        params: {
            ruleId,
            actuatorId,
            commandTrigger
        }
    }),
    removeCommandById: (id: number): Promise<AxiosResponse<Result<void>>> => api.delete('/command/removeCommandById', {params: {id}}),
    getCommandById: (id: number): Promise<AxiosResponse<Result<Command>>> => api.get('/command/getCommandById', {params: {id}}),
    getCommandByIdArray: (id: number[]): Promise<AxiosResponse<Result<Command[]>>> => api.get('/command/getCommandByIdArray', {params: {id: id.join(',')}}),
    getCommandByActuatorId: (actuatorId: number): Promise<AxiosResponse<Result<Command[]>>> => api.get('/command/getCommandByActuatorId', {params: {actuatorId}}),
    getCommandByRuleId: (ruleId: number): Promise<AxiosResponse<Result<Command[]>>> => api.get('/command/getCommandByRuleId', {params: {ruleId}}),
    getAllCommands: (): Promise<AxiosResponse<Result<Command[]>>> => api.get('/command/getAllCommands'),
};

export const DataApi = {
    addData: (data: Data): Promise<AxiosResponse<Result<void>>> => api.post('/data/addData', data),
    addDataSimple: (equipmentId: number, dataTypeId: number, time: Date | null, value: number): Promise<AxiosResponse<Result<void>>> => api.post('/data/addDataSimple', {
        equipmentId,
        dataTypeId,
        time: time?.getTime(),
        value
    }),
    addDataList: (dataList: Data[]): Promise<AxiosResponse<Result<void>>> => api.post('/data/addDataList', dataList),
    getDataById: (id: number): Promise<AxiosResponse<Result<Data>>> => api.get('/data/getDataById', {params: {id}}),
    getAllData: (): Promise<AxiosResponse<Result<Data[]>>> => api.get('/data/getAllData'),
    getData: (equipmentId: number, dataTypeId: number, start: Date, end: Date): Promise<AxiosResponse<Result<Data[]>>> => api.get('/data/getData', {
        params: {
            equipmentId,
            dataTypeId,
            start: start.getTime(),
            end: end.getTime(),
        }
    }),
    getDataToDataSheet: (dataTypeId: number, equipmentIdArray: number[], timeStep: number, startTime: Date, endTime: Date): Promise<AxiosResponse<Result<DataSheet>>> => api.get('/data/getDataToDataSheet', {
        params: {
            dataTypeId,
            equipmentIdArray: equipmentIdArray.join(','),
            timeStep,
            startTime: startTime.getTime(),
            endTime: endTime.getTime(),
        }
    }),
};

export const DataTypeApi = {
    registerDataType: (name: string): Promise<AxiosResponse<Result<void>>> => api.post('/dataType/register', null, {params: {name}}),
    removeDataTypeById: (id: number): Promise<AxiosResponse<Result<void>>> => api.delete('/dataType/removeDataTypeById', {params: {id}}),
    getDataTypeById: (id: number): Promise<AxiosResponse<Result<DataType>>> => api.get('/dataType/getDataTypeById', {params: {id}}),
    getDataTypeByName: (name: string): Promise<AxiosResponse<Result<DataType>>> => api.get('/dataType/getDataTypeByName', {params: {name}}),
    getAllDataType: (): Promise<AxiosResponse<Result<DataType[]>>> => api.get('/dataType/getAllDataType'),
    getDataTypeByIdArray: (idArray: number[]): Promise<AxiosResponse<Result<DataType[]>>> => api.get('/dataType/getDataTypeByIdArray', {params: {id: idArray.join(',')}}),
    getDataTypeByNameArray: (nameArray: string[]): Promise<AxiosResponse<Result<DataType[]>>> => api.get('/dataType/getDataTypeByNameArray', {params: {name: nameArray.join(',')}}),
};

export const EquipmentApi = {
    registerEquipment: (name: string): Promise<AxiosResponse<Result<void>>> => api.post('/equipment/registerEquipment', null, {params: {name}}),
    removeEquipmentPosById: (equipmentId: number): Promise<AxiosResponse<Result<void>>> => api.delete('/equipment/removeEquipmentPosById', {params: {equipmentId}}),
    updateEquipmentAnotherNameById: (id: number, anotherName: string): Promise<AxiosResponse<Result<void>>> => api.put('/equipment/updateEquipmentAnotherNameById', null, {
        params: {
            id,
            anotherName
        }
    }),
    updateEquipmentTimeById: (id: number): Promise<AxiosResponse<Result<void>>> => api.put('/equipment/updateEquipmentTimeById', null, {params: {id}}),
    getEquipmentById: (id: number): Promise<AxiosResponse<Result<Equipment>>> => api.get('/equipment/getEquipmentById', {params: {id}}),
    getEquipmentByName: (name: string): Promise<AxiosResponse<Result<Equipment>>> => api.get('/equipment/getEquipmentByName', {params: {name}}),
    getAllEquipment: (): Promise<AxiosResponse<Result<Equipment[]>>> => api.get('/equipment/getAllEquipment'),
    getEquipmentByIdArray: (idArray: number[]): Promise<AxiosResponse<Result<Equipment[]>>> => api.get('/equipment/getEquipmentByIdArray', {params: {id: idArray.join(',')}}),
    getEquipmentByNameArray: (nameArray: string[]): Promise<AxiosResponse<Result<Equipment[]>>> => api.get('/equipment/getEquipmentByNameArray', {params: {name: nameArray.join(',')}}),
};

export const RuleApi = {
    registerRule: (rule: Rule): Promise<AxiosResponse<Result<void>>> => api.post('/rule/registerRule', rule),
    deleteByID: (id: number): Promise<AxiosResponse<Result<void>>> => api.delete('/rule/deleteByID', {params: {id}}),
    updateById: (id: number, rule: Rule): Promise<AxiosResponse<Result<void>>> => api.put('/rule/updateById', rule, {params: {id}}),
    getRuleById: (id: number): Promise<AxiosResponse<Result<Rule>>> => api.get('/rule/getRuleById', {params: {id}}),
    getRuleByEquipmentId: (equipmentId: number): Promise<AxiosResponse<Result<Rule[]>>> => api.get('/rule/getRuleByEquipmentId', {params: {equipmentId}}),
    getRuleByDataTypeId: (dataTypeId: number): Promise<AxiosResponse<Result<Rule[]>>> => api.get('/rule/getRuleByDataTypeId', {params: {dataTypeId}}),
    getRuleByEquipmentIdArray: (idArray: number[]): Promise<AxiosResponse<Result<Rule[]>>> => api.get('/rule/getRuleByEquipmentIdArray', {params: {id: idArray.join(',')}}),
    getAllRule: (): Promise<AxiosResponse<Result<Rule[]>>> => api.get('/rule/getAllRule'),
};

export const ActuatorApi = {
    registerActuator: (name: string): Promise<AxiosResponse<Result<void>>> => api.post('/actuator/registerActuator', {name}),
    removeActuatorById: (id: number): Promise<AxiosResponse<Result<void>>> => api.delete('/actuator/removeActuatorById', {params: {id}}),
    getActuatorById: (id: number): Promise<AxiosResponse<Result<Actuator>>> => api.get('/actuator/getActuatorById', {params: {id}}),
    getActuatorByName: (name: string): Promise<AxiosResponse<Result<Actuator>>> => api.get('/actuator/getActuatorByName', {params: {name}}),
    getAllActuator: (): Promise<AxiosResponse<Result<Actuator[]>>> => api.get('/actuator/getAllActuator'),
    getActuatorByIdArray: (idArray: number[]): Promise<AxiosResponse<Result<Actuator[]>>> => api.get('/actuator/getActuatorByIdArray', {params: {id: idArray.join(',')}}),
    getActuatorByNameArray: (nameArray: string[]): Promise<AxiosResponse<Result<Actuator[]>>> => api.get('/actuator/getActuatorByNameArray', {params: {name: nameArray.join(',')}}),
};

export enum ResultType {
    SUCCESSFUL = "SUCCESSFUL",
    FAIL = "FAIL",
    ERROR = "ERROR"
}

export function getResultTypeFromString(name: string): ResultType | undefined {
    return ResultType[name as keyof typeof ResultType];
}

export interface Result<T = any> {
    resultType: string //请去响应一个ResultType
    message: string
    data: T
}

export interface Data {
    id: number;
    detectionId: number;
    dataTypeId: number;
    time: string; // 请求响应一个时间戳 类似 "2024-04-28T13:10:36.607+00:00" 这边不做转换
    value: number; // float 在 TypeScript 中对应 number 类型
}

export interface DataType {
    id: number
    name: string
}

export interface Actuator {
    id: number
    name: string
}

export interface Equipment {
    id: number
    name: string
    longitude: number
    latitude: number
    upTime: number
}

export interface DataSheet {
    dataType: DataType;
    timeStep: number;
    startTime: number;
    endTime: number;

    equipmentList: Equipment[];
    timestampList: number[];

    //equipment -> timestamp
    value: number[][];
}

export interface Command {
    id: number;
    ruleId: number;
    actuatorId: number;
    commandTrigger: number;
}

export interface Rule {
    id: number;
    datatypeId: number;
    equipmentId: number;

    exceptionUpper: number;
    warnUpper: number;
    warnLower: number;
    exceptionLower: number;

    warnSendMessage: boolean;
    exceptionSendMessage: boolean;
}