import axios, {AxiosResponse} from 'axios';

export function registerEquipment(): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: '/equipment/registerEquipment',
        method: 'POST',
    })
}

export function removeEquipmentPosById(id: number): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: '/equipment/removeEquipmentPosById',
        method: 'DELETE',
        params: {id}
    })
}

export function getAllEquipment(): Promise<AxiosResponse<Result<Equipment[]>, any>> {
    return axios({
        url: '/equipment/getAllEquipment',
        method: 'GET'
    })
}

export function getEquipmentById(id: number): Promise<AxiosResponse<Result<Equipment>, any>> {
    return axios({
        url: '/equipment/getEquipmentById',
        method: 'GET',
        params: {id}
    })
}

export function getEquipmentByIdArray(id: number[]): Promise<AxiosResponse<Result<Equipment[]>, any>> {
    return axios({
        url: '/equipment/getEquipmentByIdArray',
        method: 'GET',
        params: {id: id.join(',')}
    })
}


export function updateEquipmentAnotherNameById(id: number, anotherName: string): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: '/equipment/updateEquipmentAnotherNameById',
        method: 'PUT',
        params: {id, anotherName}
    })
}

export function updateEquipmentTimeById(id: number): Promise<AxiosResponse<Result<undefined>, any>> {

    return axios({
        url: '/equipment/updateEquipmentTimeById',
        method: 'PUT',
        params: {id}
    })
}

export function removeDataTypeById(id: number): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: "/dataType/removeDataTypeById",
        method: "DELETE",
        params: {id}
    });
}

export function getAllDataType(): Promise<AxiosResponse<Result<DataType[]>, any>> {
    return axios({
        url: "/dataType/getAllDataType",
        method: 'GET'
    });
}

export function getDataTypeById(id: number): Promise<AxiosResponse<Result<DataType>, any>> {
    return axios({
        url: "/dataType/getDataTypeById",
        method: "GET",
        params: {id}
    });
}

export function updateDataTypeAnotherName(id: number, anotherName: string): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: "/dataType/updateDataTypeAnotherName",
        method: "PUT",
        params: {id, anotherName}
    })
}

export function registerData(data: Data): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: '/data/addData',
        method: 'POST',
        data: data
    })
}

export function addDataSimple(equipmentId: number, dataTypeId: number, value: number): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: '/data/addDataSimple',
        method: 'POST',
        params: {equipmentId, dataTypeId, value}
    })
}

export function addDataList(dataList: Data[]): Promise<AxiosResponse<Result<undefined>, any>> {
    return axios({
        url: '/data/addDataList',
        method: 'POST',
        data: dataList
    })
}

export function getDataById(id: number): Promise<AxiosResponse<Result<Data>, any>> {
    return axios({
        url: '/data/getDataById',
        method: 'GET',
        params: {id}
    })
}

export function getAllData(): Promise<AxiosResponse<Result<Data[]>, any>> {
    return axios({
        url: '/data/getAllData',
        method: 'POST'
    })
}

export function getData(
    equipmentId: number,
    dataTypeId: number,
    start: number,
    end: number
): Promise<AxiosResponse<Data[], any>> {
    return axios({
        url: '/data/getData',
        method: 'GET',
        params: {equipmentId, dataTypeId, start, end}
    })
}

export function getDataToDataSheet(dataFilter: DataFilter): Promise<AxiosResponse<Result<DataSheet>>> {
    return axios({
        url: '/data/getDataToDataSheet',
        method: 'POST',
        data: dataFilter
    })
}


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

export interface Equipment {
    id: number
    name: string
    longitude: number
    latitude: number
    upTime: string
}

export interface DataScreening {
    dataType: DataType
    equipmentList: Equipment[]
    timeStep: number
    startTime: Date
    endTime: Date
}

export interface DataSheet {
    dataType: DataType;
    timeStep: number;
    startTime: Date;
    endTime: Date;

    equipmentList: Equipment[];
    timestampList: string[];

    //equipment -> timestamp
    value: number[][];
}

export interface DataFilter {
    dataTypeId: number;
    equipmentIdArray: number[];
    timeStep: number;
    startTime: Date;
    endTime: Date;
}
